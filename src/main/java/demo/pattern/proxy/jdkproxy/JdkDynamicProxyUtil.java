package demo.pattern.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author malaka
 * @create 2020-12-14 11:22
 */
public class JdkDynamicProxyUtil { // 这就是一个手写的工具类，直接用Proxy.newProxyInstance（...)也行
    public static <T>T newProxyInstance(T targetObject, InvocationHandler handler){  //客户端中： targetObject会传入被代理对象，handler传入handler接口实现类对象
        ClassLoader classLoader = targetObject.getClass().getClassLoader(); // 被代理对象 的 类加载器
        Class<?>[] interfaces = targetObject.getClass().getInterfaces();  // 被代理对象 实现的接口
        //动态代理的原理？——去阅读Proxy的源码，
        // byte[] proxyClassFile = ProxyGenerator.generateProxyClass  会生成$Proxy二进制流
        //返回代理角色；
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
}
