package demo.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-06-09 20:01
 **/
public class ConstructorCollector {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class clazz = Class.forName("demo.reflect.ReflectTarget");
        //1、获取所有的公有构造方法
        System.out.println("*********************所有公有构造方法************************");
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }


        //2、获取所有构造方法
        System.out.println("\n*************所有的构造方法（包括私有、受保护、公有、默认）*****************");
        constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }


        //3、获取单个带参数的公有方法
        System.out.println("\n***************获取公有、有两个参数的构造方法*********************");
        Constructor con = clazz.getConstructor(String.class,int.class);
        System.out.println("con:" + con);


        //4、获取单个私有的构造方法
        System.out.println("\n******************获取私有构造方法************************");
        con = clazz.getDeclaredConstructor(int.class);
        System.out.println("print con = " + con);


        System.out.println("\n****************调用私有构造方法创建实例**********************");
        //暴力访问（忽略掉访问的修饰符）
        con.setAccessible(true);
        ReflectTarget reflectTarget = (ReflectTarget) con.newInstance(1);
    }

}
