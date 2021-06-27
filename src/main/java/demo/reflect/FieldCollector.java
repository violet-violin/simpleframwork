package demo.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-12 21:13
 **/
public class FieldCollector {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //获取Class对象
        Class reflectTargetClass = Class.forName("demo.reflect.ReflectTarget");

        //1、获取所有公有的字段
        System.out.println("***************获取所有公有的字段****************");
        Field[] fieldArray = reflectTargetClass.getFields();
        for (Field field : fieldArray) {
            System.out.println(field);
        }


        //2、获取所有的字段
        System.out.println("\n***************获取所有的字段");
        Field[] declaredFields = reflectTargetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }


        //3、获取单个特定公有的fileId
        System.out.println("\n*************获取某个公有字段并调用*********");
        Field name = reflectTargetClass.getField("name");
        System.out.println("公有的属性："+name);
        ReflectTarget reflectTarget = (ReflectTarget) reflectTargetClass.getConstructor().newInstance();

        //4、给获取到的field赋值
        name.set(reflectTarget,"待反射一号");
        //5、验证对应值的name
        System.out.println("验证name：" + reflectTarget.name);


        //6、获取单个私有的Field
        System.out.println("\n**************获取某个私有字段targetInfo并且调用*****************");
        name = reflectTargetClass.getDeclaredField("targetInfo");
        name.setAccessible(true);
        System.out.println(name);

        name.set(reflectTarget,"14232323423");
        System.out.println("验证信息" + reflectTarget);

    }

}
