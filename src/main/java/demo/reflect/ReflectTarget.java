package demo.reflect;

import lombok.ToString;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 19:38
 **/

@ToString
public class ReflectTarget extends ReflectTargetOrigin {

    //-----------构造函数-----------------

    //(默认的带参数构造函数)
    ReflectTarget(String str) {
        System.out.println("(默认)的构造方法 s =" + str);
    }

    //(无参构造函数)
    public ReflectTarget() {
        System.out.println("调用了公有的无参构造方法。。。");
    }

    //有一个参数的构造函数
    public ReflectTarget(char name) {
        System.out.println("调用了带有一个参数的构造方法，参数值为：" +name);
    }

    //有多个参数的构造函数
    public ReflectTarget(String name,int index) {
        System.out.println("调用了带有多个参数的构造方法，参数值为【目标名】：" + name + "【序号】:" + index);
    }


    //受保护的构造函数
    protected ReflectTarget(boolean flag) {
        System.out.println("受保护的构造函数flag:" + flag);
    }

    //私有的构造函数
    private ReflectTarget(int index) {
        System.out.println("私有的构造方法 index : " + index);
    }


    //**************字段*********************//
    public String name;
    protected int index;
    char type;
    private String targetInfo;


    //*****************成员方法*****************//
    public void show1(String s) {
        System.out.println("调用了公有的，String参数的show()1:s=" + s);
    }

    public void show2() {
        System.out.println("调用了受保护的，无参的show2()");
    }

    void show3() {
        System.out.println("调用了默认的，无参的show3()");
    }

    private String show4(int index) {
        System.out.println("调用了私有的，并且有返回值的，int参数的show4();index = " + index);
        return "show4result";
    }



    public static void main(String[] args) throws ClassNotFoundException {

        //第一种方式获得Class对象
        ReflectTarget reflectTarget = new ReflectTarget();
        Class reflectTargetClass1 = reflectTarget.getClass();
        System.out.println("1st:"+reflectTargetClass1.getName());


        //第二种方式获取Class对象
        Class<ReflectTarget> reflectTargetClass2 = ReflectTarget.class;
        System.out.println("2nd:"+reflectTargetClass2.getName());

        //判断第一种方式获取的class对象和第二种方式获取的是否同一个
        System.out.println(reflectTargetClass1 == reflectTargetClass2);
        System.out.println(reflectTargetClass1.equals(reflectTargetClass2));

        //第三种方式获取Class
        Class reflectTargetClass3 = Class.forName("demo.reflect.ReflectTarget");
        System.out.println("3third:"+reflectTargetClass3.getName());
        System.out.println(reflectTargetClass2 == reflectTargetClass3);

    }

}
