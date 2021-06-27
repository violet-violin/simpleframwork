package demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author malaka
 * @create 2020-12-03 10:39
 */
public class AnnotationParser {
    //解析类的注解
    public static void parseTypeAnnotation() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("demo.annotation.ImoocCourse");

        //这里获取的是class对象的对应类型的类注解，而不是其里面的方法和成员变量的注解
        Annotation[] annotations = clazz.getAnnotations();

        for(Annotation annotation : annotations){
            CourseInfoAnnotation courseInfoAnnotation = (CourseInfoAnnotation) annotation;
            System.out.println("课程名：" + courseInfoAnnotation.courseName()
                    + " 标签：" + courseInfoAnnotation.courseTag()
                    + " 简介：" + courseInfoAnnotation.courseProfile()
                    + " 序号: " + courseInfoAnnotation.courseIndex());
        }
    }

    //解析属性注解
    public static  void parseFieldAnnotation() throws ClassNotFoundException{
        Class<?> clazz = Class.forName("demo.annotation.ImoocCourse");

        Field[] fields = clazz.getDeclaredFields();

        for(Field f : fields){
            //判断成员变量中是否有指定注解类型的注解
            boolean hasAnnotation = f.isAnnotationPresent(PersonInfoAnnotation.class);
            if(hasAnnotation){
                PersonInfoAnnotation annotation = f.getAnnotation(PersonInfoAnnotation.class);
                System.out.println("名字：" + annotation.name() +
                        " 年龄: " + annotation.age() +
                        " 性别: " + annotation.gender() + " ");
                for(String language: annotation.language()){
                    System.out.print("课程名：" + language + " ");
                }
            }
        }
    }

    //解析方法注解
    public static void parseMethodAnnotation() throws ClassNotFoundException{
        Class<?> clazz = Class.forName("demo.annotation.ImoocCourse");

        Method[] methods = clazz.getDeclaredMethods();

        for(Method method : methods){
            //判断方法中是否有指定注解类型的注解
            boolean hasAnnotation = method.isAnnotationPresent(CourseInfoAnnotation.class);
            if(hasAnnotation){
                CourseInfoAnnotation courseInfoAnnotation = method.getAnnotation(CourseInfoAnnotation.class);
                System.out.println("课程名：" + courseInfoAnnotation.courseName()
                        + " 标签：" + courseInfoAnnotation.courseTag()
                        + " 简介：" + courseInfoAnnotation.courseProfile()
                        + " 序号: " + courseInfoAnnotation.courseIndex());
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException {
//        parseTypeAnnotation();
        parseFieldAnnotation();
//        parseMethodAnnotation();
    }



}
