package org.simpleframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 往被代理的方法添加横切逻辑
 * @author malaka
 * @create 2020-12-14 15:04
 */
//@Data
//@NoArgsConstructor
public class AspectListExecutor implements MethodInterceptor {  //对每个被代理的对象进行方法的拦截
    //被代理的类
    private Class<?> targetClass;

    //按照order注解的value值，排好序的Aspect列表，value小的先织入；//切面信息集合 照顾多个AOP的 情况
    @Getter
    private List<AspectInfo> sortedAspectInfoList;  // AspectInfo类 集合了抽象类DefaultAspect的实现类以及Order注解的值


    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList){
        this.targetClass = targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList); // 按照order的值进行升序排序，确保order值小的aspect先被织入；直接就进入构造函数
    }

    /**
     *  按照order的值进行升序排序，确保order值小的aspect先被织入
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                // 按照order值的大小进行升序排判
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

//    @Override // aop 1.0
//    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
//        Object returnValue = null;
//        if(ValidationUtil.isEmpty(sortedAspectInfoList)){
//            return returnValue;  //Aspect列表为空，就直接返回空值，不进行织入
//        }
//        //1.按照order的顺序升序执行完所有Aspect的before方法
//        invokeBeforeAdvice(method, args);
//        try {
//            //2.执行被代理类的方法 ———— methodProxy.invokeSuper
//            returnValue = methodProxy.invokeSuper(proxy, args);//proxy是动态代理对象实例
//            //3.如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
//            returnValue = invokeAfterReturningAdvice(method, args, returnValue);
//        } catch (Throwable e) {
//            //4.如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
//            invokeAfterThrowingAdvice(method, args, e);
//        }
//        return returnValue;   //这个returnValue是啥？返回了能干啥？
//    }

    /**
     * aop 2.0 重写的 cglib 动态代理中 MethodInterceptor 接口的intercept方法
     * @param proxy 被增强的对象
     * @param method 需要拦截的方法，即被代理对象的被拦截的方法
     * @param args 方法参数，即被代理对象方法的参数集合
     * @param methodProxy 代理方法
     * @return 返回值
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        // 下一行是aop 2.0 代码 精确筛选
        collectAccurateMatchedAspectList(method);  //精筛，对sortedAspectInfoList进行方法级别的精筛，精筛后的AspectInfo就按 之前一样来织入即可
        if(ValidationUtil.isEmpty(sortedAspectInfoList)){
            // 如果精筛后没有需要执行的切面，就执行被代理类的原方法即可，然后返回；（无论如何都要执行methodProxy.invokeSuper）
            returnValue = methodProxy.invokeSuper(proxy, args);
            return returnValue;
        }
        //1.按照order的顺序升序执行完所有Aspect的before方法
        invokeBeforeAdvice(method, args);
        try {
            //2.执行被代理类的方法 ———— methodProxy.invokeSuper
            returnValue = methodProxy.invokeSuper(proxy, args);//proxy是动态代理对象实例
            //3.如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
            returnValue = invokeAfterReturningAdvice(method, args, returnValue);
        } catch (Throwable e) {
            //4.如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvice(method, args, e);
        }
        return returnValue;   //这个returnValue是啥？返回了能干啥？
    }

    /**
     * 精确匹配 留下符合精确匹配的结果  //aop 2.0
     * @param method
     */
    private void collectAccurateMatchedAspectList(Method method) {
        if(ValidationUtil.isEmpty(sortedAspectInfoList)) return;
        //foreach（工作在独立的线程里，mutex锁，fail-fast原则）报并发修改异常，用迭代器方式来遍历
        // 由于需要边遍历边删除sortedAspectInfoList 列表中不符合的 AspectInfo(Aspect切面类)，所以这里用迭代器
        Iterator<AspectInfo> it = sortedAspectInfoList.iterator();  // 可能一个被织入类targetClass有多个Aspect，就要遍历
        while (it.hasNext()){
            AspectInfo aspectInfo = it.next();
            //区别两种方式
            //如果注解不为空 校验注解
            //精确校验 对传入的Method实例进行筛选，看看当前Method实例是否需要织入横切逻辑；该方法支持对AspectJ的`各种语法树的精确匹配`。
            // accurateMatches 底层是 pointcutExpression.matchesMethodExecution(method);
            if (!aspectInfo.getPointcutLocator().accurateMatches(method)) {
//                完成对粗筛列表的精筛；粗筛啥(within可以筛掉)
//                精筛啥（execution、call，get，set等表达式可以筛掉），方法级别的精筛，看PointcutLocator的方法的定义
                it.remove();
            }
        }
    }


    //1.按照order的顺序升序执行完所有Aspect的before方法
    private void invokeBeforeAdvice(Method method, Object[] args) throws Throwable {
        for(AspectInfo aspectInfo : sortedAspectInfoList){
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }
    }
    //3.如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
    private Object invokeAfterReturningAdvice(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result = null;
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--){ // 倒序遍历aspect列表
            //.afterReturning方法  需要DefaultAspect子类自定义
            result = sortedAspectInfoList.get(i).getAspectObject().afterReturning(targetClass,method,args,returnValue);
        }
        return result;
    }

    //4.如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
    private void invokeAfterThrowingAdvice(Method method, Object[] args, Throwable e) throws Throwable {
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--){
            //.afterReturning方法 需要DefaultAspect子类自定义
             sortedAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass,method,args,e);
        }
    }
}
