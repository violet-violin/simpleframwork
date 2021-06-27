package demo.pattern.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author malaka
 * @create 2020-12-14 11:18
 */
public class AlipayInvocationHandler implements InvocationHandler {// 实现这个基础类接口  InvocationHandler
// 切面逻辑就定义在该接口的实现类里，其实现类就类似spring的标注@Aspect注解的类

    private Object targetObject;

    public AlipayInvocationHandler(Object targetObject){  // 形参是被代理类,// targetObject 为 被代理类的实例
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforePay();
        Object result = method.invoke(targetObject, args); // 调用被代理类的方法
        afterPay();
        return result;
    }



    private void beforePay(){
        System.out.println("从招行取款");
    }
    private void afterPay(){
        System.out.println("支付给慕课网");
    }
}
