package demo.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-13 11:31
 **/
public class MethodCollector {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //1、获取Class对象
        Class reflectTargetClass = Class.forName("demo.reflect.ReflectTarget");

        //2、获取所有的方法
        System.out.println("*********************获取所有的public方法，包括父类和Object*****************");
        Method[] methodArray = reflectTargetClass.getMethods();

        for (Method method : methodArray) {
            System.out.println(method);
        }

        //3、获取该类的所有方法
        System.out.println("\n************获取所有的方法，包括私有的*****************");
        methodArray = reflectTargetClass.getDeclaredMethods();
        for (Method method : methodArray) {
            System.out.println(method);
        }


        //4、获取单个公有的方法
        System.out.println("\n********************获取公有的show1()方法*************************");
        Method show1 = reflectTargetClass.getMethod("show1", String.class);
        System.out.println("单个公有方法："+show1);

        //5、调用show1并执行
        ReflectTarget reflectTarget = (ReflectTarget) reflectTargetClass.getConstructor().newInstance();
        show1.invoke(reflectTarget,"待反射方法一号");


        //6、获取一个私有的成员方法
        System.out.println("\n******************获取私有的show4()方法*********************");
        show1 = reflectTargetClass.getDeclaredMethod("show4", int.class);
        show1.setAccessible(true);
        System.out.println(show1);

        String result = (String) show1.invoke(reflectTarget, 20);
        System.out.println("返回值:"+result);
    }

}
