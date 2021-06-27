package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.mvc.annotation.RequestMapping;
import org.simpleframework.mvc.annotation.RequestParam;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.render.impl.ResourceNotFoundResultRender;
import org.simpleframework.mvc.type.ControllerMethod;
import org.simpleframework.mvc.type.RequestPathInfo;
import org.simpleframework.util.ConverterUtil;
import org.simpleframework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * controller请求处理器————负责将请求转发给对应的controller方法进行处理
 *
 * @author malaka
 * @create 2020-12-21 11:17
 */
@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {
    //IOC容器
    private BeanContainer beanContainer;
    //请求（路径、方法参数）和controller方法（controller的class、method及其参数）的映射的集合；ConcurrentHashMap————支持多线程的操作
    // 将映射关系 放入内存，共后面使用
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap = new ConcurrentHashMap<>();

    //无参构造函数 依靠容器的能力，建立起请求路径、请求方法与Controller方法实例的映射
    public ControllerRequestProcessor() {
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class); // 得到 所有被@RequestMapping标记的类，controller类集合
        initPathControllerMethodMap(requestMappingSet);//解析Set这个映射关系，同时将这个映射关系存放到属性pathControllerMethodMap中
    }

    //逻辑太多，课下按照四个步骤，抽取出来4个方法，让逻辑清楚点
    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtil.isEmpty(requestMappingSet)) {
            return;
        }
        //1.遍历 所有被@RequestMapping标记的类，获取类上面该注解的属性值作为一级路径
        for (Class<?> requestMappingClass : requestMappingSet) {
            RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);

            String basePath = requestMapping.value();
            if (!basePath.startsWith("/")) {//给basePath前面加个"/"
                basePath = "/" + basePath;    // 如 /fronted
            }
            //2. 遍历类里面所有被@RequestMapping标记的方法，获取方法上面该注解的属性值，作为二级路径
            Method[] methods = requestMappingClass.getDeclaredMethods();//获取所有的方法
            if (ValidationUtil.isEmpty(methods)) {
                continue;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) { // 判断 方法上是否有 RequestMapping注解
                    RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                    String methodPath = methodRequest.value();
                    if (!methodPath.startsWith("/")) { //二级路径
                        methodPath = "/" + methodPath;   // 如 /getmainpageinfo
                    }
                    String url = basePath + methodPath;//拼接一、二级路径       /fronted/getmainpageinfo

                    //3.解析方法里被@RequestParam标记的参数，会在controllerMethod构造参数里用到
                    //获取该注解的属性值，作为参数名，
                    //获取被标记的参数的数据类型，建立参数名和参数类型的映射
                    Map<String, Class<?>> methodParams = new HashMap<>();
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtil.isEmpty(parameters)) { //方法里的参数列表不为空，就对参数进行逐个解析；为空就跳过
                        for (Parameter parameter : parameters) {
                            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                            //为了实现上的简单，目前暂定简易框架为Controller方法里面所有的参数都需要@RequestParam注解；//规定形参必须要有这个 注解
                            if (requestParam == null) {  //  if (requestMapping == null){   先写错了吧，是if (requestParam == null){ 才对
                                throw new RuntimeException("The parameter must have @RequestParam，方法参数必须被RequestParam注解标记");
                            }
                            //@RequestParam注解的值  和 形参对应类型，放入map
                            methodParams.put(requestParam.value(), parameter.getType());
                        }
                    }

                    //4.将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放置到映射表里
                    //TODO
                    String httpMethod = String.valueOf(methodRequest.method());//枚举类型转为String类型，得到请求方法GET/POST
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    if (pathControllerMethodMap.containsKey(requestPathInfo)) {
                        //告诉用户存在相同的路径定义（url、方法）  默认允许这种情况发生
                        log.warn("duplicate url:{} registration, current class {} method {} will override the former one（存在相同的路径定义，存在于不同的类和方法里面，旧的会被覆盖）",
                                requestPathInfo.getHttpPath(), requestMappingClass.getName(), method.getName());
                    }
                    //这个controllerMethod 的 实例对反射调用method方法很有用
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    this.pathControllerMethodMap.put(requestPathInfo, controllerMethod);  // 建立起 映射 关系
                }
            }
        }
    }

    // controller 方法 请求的处理
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //1.解析FHttpServletRequest请求方法 请求路径 获取对应的ControllerMethod实例
        String path = requestProcessorChain.getRequestPath();
        String method = requestProcessorChain.getRequestMethod();//这边的path、method是从req域里取出来，相当于从前端页面取出来的

        RequestPathInfo requestPathInfo = new RequestPathInfo(method, path);
        //这是根据前端传递的path、method找到对应的controller方法 ControllerMethod 的实例
        ControllerMethod controllerMethod = pathControllerMethodMap.get(requestPathInfo);
        if (controllerMethod == null) {//没有  请求与controller方法的映射，转ResourceNotFoundResultRender
            //后续ResourceNotFoundResultRender根据method、path进行渲染
            //针对请求路径path、请求方法method，没有找到资源，责任链模式结束
            //ResourceNotFoundResultRender 渲染器 会给 resp 域返回一个404 状态码
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method, path));
            return false;
        }

        //2.解析请求参数，传递给获取到的ControllerMethod实例去执行，并得到 请求的处理结果 Object result
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        //3.根据解析的结果，选择对应的render进行渲染；选择 渲染器
//        setResultRender(result,controllerMethod,requestProcessorChain);//视频里是这样，我把setResultRender写到requestProcessorChain类里
        requestProcessorChain.setResultRender(result, controllerMethod, requestProcessorChain);
        return true;
    }

//    /**
//     * 根据不同情况设置不同的渲染器  我把这个 设置渲染器的方法 放到requestProcessorChain下面了
//     * @param result
//     * @param controllerMethod
//     * @param requestProcessorChain
//     */
//    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
//    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        //1.从请求里获取GET 或者 POST 的参数名及其对应的值
        Map<String, String> requestParamMap = new HashMap<>();//存储 从请求里解析出来的参数名、及其对应的值（username=leon）
        //GET，POST方法的请求参数获取方式；该方法只能获取GET、POST的请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();//request域里获取前端传来的参数名、及其对应值???getParameterMap()效用？？
        for (Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            if (!ValidationUtil.isEmpty(parameter.getValue())) {
                //为了实现上的简单，只支持一个参数对应一个值的形式
                requestParamMap.put(parameter.getKey(), parameter.getValue()[0]);
            }
        }

        //2.根据获取到的请求参数名及其对应的值，以及controllerMethod里面的参数和类型的映射关系，去实例化出方法对应的参数并放入ArrayList methodParams 中
        List<Object> methodParams = new ArrayList<>();
        //从后端，获取controllerMethod保存的方法里的 @RequestParam注解的值、及其对应的参数类型
        // controllerMethod 是从 pathControllerMethodMap 成员变量里获取的
        Map<String, Class<?>> methodParameterMap = controllerMethod.getMethodParameters();
        //paramName、type——就是后端单吗controller方法上@RequestParam注解的值和 对应形参的类型
        for (String paramName : methodParameterMap.keySet()) {
            Class<?> type = methodParameterMap.get(paramName);

            //这里是根据代码里的@RequestParam注解的值 来找前端页面的requestValue值；
            // 使用时，要求 @RequestParam注解的值 和 请求里解析出来的参数名一致，如  @RequestParam("username") String userName、username=leon；Spring也是这种要求
            String requestValue = requestParamMap.get(paramName);
            Object value; // 用value来接收 req域传来的 实例化后的 处理好的 参数值

            if (requestValue == null) {
                //将请求里的参数值转成适配于参数类型的空值      如int就要转换成0，String 转为 ""
                // 为了实现的简单，只支持String以及基础类型char，int，short，byte，double，long，float，boolean，及它们的包装类型的转换
                value = ConverterUtil.primitiveNull(type);
            } else {
                // String类型的requestValue（从req域传来的）转换成对应的参数类型————有值的转换
                value = ConverterUtil.convert(type, requestValue);
            }
            methodParams.add(value);  // 把参数值 放入 list 列表
        }

        //3.利用反射，执行Controller里面对应的方法并返回结果
        // controllerMethod.getControllerClass() 得到controller方法 对应类 的class；在 controllerMethod 里有保存
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);  // 可能有些controller方法是private的
        Object result;  // 用来保存 controller方法执行后的返回值
        try {
            if (methodParams.size() == 0) {//按照有没有传入参数来分类，controller方法 没有 参数
                result = invokeMethod.invoke(controller);//controller是invokeMethod对应的类的实例
            } else { // controller方法 有 参数
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (InvocationTargetException e) {
            //如果是调用异常的话，需要通过e.getTargetException() 去获取执行方法抛出的异常；调用异常 获取方法抛出的异常
            Throwable targetException = e.getTargetException();
            throw new RuntimeException(targetException);
        } catch (IllegalAccessException e) {
            //非法访问异常，直接处理
            throw new RuntimeException(e);
        }
        return result;  // 返回 controller 方法 处理 结果
    }
}
