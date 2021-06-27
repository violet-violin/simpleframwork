package demo.pattern.proxy;

import demo.pattern.proxy.cglib.AlipayMethodInterceptor;
import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.impl.*;
import demo.pattern.proxy.jdkproxy.AlipayInvocationHandler;
import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;

import java.lang.reflect.InvocationHandler;

/**
 * @author malaka
 * @create 2020-12-14 10:48
 */
public class ProxyDemo {

    public static void main1(String[] args) {
        ToCPayment toCPayment = new ToCPaymentImpl();  // 被代理类对象
        InvocationHandler handler = new AlipayInvocationHandler(toCPayment);  // handler对象
        ToCPayment toCProxy = JdkDynamicProxyUtil.newProxyInstance(toCPayment, handler);  // 生成代理类，
        toCProxy.pay(); // 代理类调用方法
        System.out.println("========================================");
        ToBPaymentImpl toBPayment = new ToBPaymentImpl();
        AlipayInvocationHandler handlerToB = new AlipayInvocationHandler(toBPayment);
        ToBPayment toBProxy = JdkDynamicProxyUtil.newProxyInstance(toBPayment, handlerToB);
        toBProxy.pay();
    }

    public static void main(String[] args) {
//        ToCPayment toCProxy = new AlipayToC(new ToCPaymentImpl());
//        toCProxy.pay();
//        ToBPayment toBProxy = new AlipayToB(new ToBPaymentImpl());
//        toBProxy.pay();

//        ToCPayment toCPayment = new ToCPaymentImpl();
//        InvocationHandler handler = new AlipayInvocationHandler(toCPayment);
//        ToCPayment toCProxy = JdkDynamicProxyUtil.newProxyInstance(toCPayment, handler);
//        toCProxy.pay();
//        System.out.println("========================================");
//        ToBPaymentImpl toBPayment = new ToBPaymentImpl();
//        AlipayInvocationHandler handlerToB = new AlipayInvocationHandler(toBPayment);
//        ToBPayment toBProxy = JdkDynamicProxyUtil.newProxyInstance(toBPayment, handlerToB);
//        toBProxy.pay();

        CommonPayment commonPayment = new CommonPayment();
////        jdk动态代理报错，因为CommonPayment没实现一个共同的接口；
////        因为CommonPayment没有实现接口，无法用jdk动态代理来  生成代理对象，会报错；只能用cglib
////        AlipayInvocationHandler invocationHandler = new AlipayInvocationHandler(commonPayment);
////        CommonPayment commonPaymentProxy = JdkDynamicProxyUtil.newProxyInstance(commonPayment, invocationHandler);
//
        AlipayMethodInterceptor methodInterceptor = new AlipayMethodInterceptor();
        CommonPayment commonPaymentProxy = CglibUtil.createProxy(commonPayment, methodInterceptor);
        commonPaymentProxy.pay();
//实现了接口的类的织入，也支持。
        ToCPaymentImpl toCPayment = new ToCPaymentImpl();
        ToCPaymentImpl toCPaymentProxy = CglibUtil.createProxy(toCPayment, methodInterceptor);
        toCPaymentProxy.pay();
    }

}
