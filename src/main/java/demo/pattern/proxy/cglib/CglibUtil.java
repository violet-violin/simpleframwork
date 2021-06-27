package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author malaka
 * @create 2020-12-14 13:28
 */
public class CglibUtil {
    //参数：被代理对象实例、aspect对象实例
    public static <T>T createProxy(T targetObject, MethodInterceptor methodInterceptor){
        return (T) Enhancer.create(targetObject.getClass(), methodInterceptor);//代理对象的创建
    }
}
