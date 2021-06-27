package org.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * aop1.0
 * @author malaka
 * @create 2020-12-14 14:41
 */
public abstract class DefaultAspect {//所有的切面必须继承自该类，在该类中定义我们的框架能支持几种advice

    /** 模板模式：模板方法、具体方法、钩子方法、抽象方法
     * 事前拦截；这个方法做个模板模式的钩子方法（抽象类里面的空方法，让用户来选择是否实现该钩子方法）
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @throws Throwable
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable{

    }


    /**
     *
     * 事后拦截；钩子方法
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @param returnValue 被代理的目标方法执行后的返回值
     * @throws Throwable
     */
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable{
        return returnValue;//也是空实现
    }

    /**
     *
     * 事后拦截； 钩子方法
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @param e 被代理的目标方法抛出的异常
     * @throws Throwable
     */
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable{

    }
}
