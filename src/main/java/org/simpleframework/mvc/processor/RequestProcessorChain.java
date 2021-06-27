package org.simpleframework.mvc.processor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.annotation.ResponseBody;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.render.impl.DefaultResultRender;
import org.simpleframework.mvc.render.impl.InternalErrorResultRender;
import org.simpleframework.mvc.render.impl.JsonResultRender;
import org.simpleframework.mvc.render.impl.ViewResultRender;
import org.simpleframework.mvc.type.ControllerMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * 1.以责任链模式执行注册的请求处理器
 * 2.委派给特定的render实例对处理后的结果经行渲染
 * @author malaka
 * @create 2020-12-21 11:15
 */
@Slf4j
@Data
public class RequestProcessorChain {
    //请求处理器迭代器
    private Iterator<RequestProcessor> requestProcessorIterator;;
    //请求request——servlet中传过来的
    private HttpServletRequest request;
    //请求response
    private HttpServletResponse response;
    //解析出来的请求方法
    private String requestMethod;
    //请求路径
    private String requestPath;
    //请求后的状态码——http响应状态码
    private int responseCode;
    //请求结果渲染器，用该成员变量保存 本次请求 的渲染器
    private ResultRender resultRender;

    //构造函数
    public RequestProcessorChain(Iterator<RequestProcessor> iterator, HttpServletRequest req, HttpServletResponse resp) {
        this.requestProcessorIterator = iterator;
        this.request = req;
        this.response = resp;
        //获取请求方法  如GET、POST...
        this.requestMethod = req.getMethod();
        //获取请求路径
        this.requestPath = req.getPathInfo();
        //默认成功处理 200
        this.responseCode = HttpServletResponse.SC_OK;
        // 渲染器 依赖于 ResultRender RequestProcessor矩阵的执行结果，后续再赋值
    }

    //这个构造函数拿来干嘛
    public RequestProcessorChain(Iterator<RequestProcessor> iterator, ServletRequest servletRequest, ServletResponse servletResponse) {
        this.requestProcessorIterator = iterator;
        this.request = (HttpServletRequest) servletRequest;
        this.response = (HttpServletResponse) servletResponse;
        //获取请求方法  如GET、POST...
        this.requestMethod =((HttpServletRequest) servletRequest).getMethod();
        //获取请求路径
        this.requestPath = ((HttpServletRequest) servletRequest).getPathInfo();
        //默认成功处理   200
        this.responseCode = HttpServletResponse.SC_OK;
    }

    /**
     * 以责任链模式执行请求链
     */
    public void doRequestProcessorChain() {
        try {
            //1.通过迭代器遍历注册的请求处理器实现类列表
            while (requestProcessorIterator.hasNext()){
                boolean res =  requestProcessorIterator.next()
                        .process(this); //4个处理器依次执行  像是策略模式
                //2.直到某个请求处理器执行后返回为false为止
                if (res == false){
                    break;
                }
            }
        }catch (Exception e){
            //3.期间出现异常则由内部异常渲染器 处理
            this.resultRender = new InternalErrorResultRender(e.getMessage());   //todo 像是策略模式，策略模式，啥？
            log.error("do RequestProcessorChain error:",e);
        }
    }

    /**
     * 执行处理器——渲染请求结果
     */
    public void doRender() {
        //1.如果请求处理器均没有选择合适的渲染器，则使用默认的渲染器
        if (resultRender == null){
            resultRender = new DefaultResultRender();
        }
        //2.调用渲染器的render方法对结果进行渲染
        try {
            resultRender.render(this); //像是策略模式
        }catch (Exception e){//渲染过程中抛出异常
            log.error("doRender error:",e);
            throw new RuntimeException(e);
        }
    }

    //策略模式，这就是策略模式；ResultRender没在构造函数中赋值，用set函数进行赋值
    /**
     * 根据不同请求处理结果情况   设置不同的渲染器
     * @param result  请求 对应的controller方法，通过反射调用它后，得到的结果
     * @param controllerMethod
     * @param requestProcessorChain
     */
    public void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null){  // controller方法执行后，result == null，就 不进行渲染，用DefaultResultRender
            return;
        }
        ResultRender resultRender;
        //判断一个方法上面是否有@RespenseBody标签，是则直接返回JSON数据
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson){
            resultRender = new JsonResultRender(result); // 用 json 渲染器
        }else {//不是json，结果返回页面
            resultRender = new ViewResultRender(result);  // 用 视图 渲染器 ViewResultRender
        }
        requestProcessorChain.setResultRender(resultRender);
    }
}
