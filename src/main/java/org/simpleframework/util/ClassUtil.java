package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author malaka
 * @create 2020-11-30 21:01
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCAL = "file";  // file————代表文件类型的资源

    /**
     * 获取包下类集合 ———— 获取目标package里面的所有class文件
     *
     * @param packageName
     * @return 类集合
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {

        //1.获取到类的加载器。
        ClassLoader classLoader = getClassLoader();
        //2.通过类加载器获取到加载的资源；String#replace —— 用 / 换 .   //用户传入的packageName是 . 隔开的，故要替换为 /
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) { // url的空值判断  "file:/D:/workspace_idea1/simpleframework/target/classes/com/imooc/entity
            log.warn("unable to retrieve anything from package" + packageName);
            return null;
        }
        //3.依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet = null;
        //过滤出文件类型的资源 —— "file"
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCAL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = new File(url.getPath());
            //递归获取目标package里面的所有class文件（包括子package里的class文件） ；
            //去 packageDirectory路径里遍历得到所有的class对象，并结合packageName生成对应class实例；
            //然后将实例放入classSet并返回classSet
            extractClassFile(classSet, packageDirectory, packageName);
        }

        //todo: 此处可以加入针对其他类型资源的处理 比如处理jar的资源，将项目打包成jar后也可以用
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件（包括子package里的class文件）
     * 去 packageDirectory路径里遍历得到所有的class对象，并结合packageName生成对应class实例；
     * 然后将实例放入classSet里
     *
     * @param emptyClassSet 装载目标类的集合
     * @param fileSource    文件或者目录
     * @param packageName   包名   //这个感觉没有用啊
     * @return 类集合
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {  // 递归终止条件
            return;
        }
        //如果是一个文件夹，则调用其listFiles方法获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {  // FileFilter 匿名类
            @Override
            public boolean accept(File file) {  // 通过FileFilter 的accept方法 过滤获取到的结果
                if (file.isDirectory()) {  // 我们 关注的是文件夹、class文件
                    return true;  // true —— 代表我们要提取 出来 该file, 放入 File[] files 中
                } else {
                    //获取文件的绝对值路径
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith(".class")) {  // 代表这是class文件
                        //若是class文件，则直接加载
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }

            //根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
            private void addToClassSet(String absoluteFilePath) {
                //1.从class文件的绝对值路径里提取出包含了package的类名
                //如/Users/baidu/imooc/springframework/simpleframework/target/classes/com/imooc/entity/dto/MainPageInfoDTO.class
                //需要弄成com.imooc.entity.dto.MainPageInfoDTO       //windows  是  "\"  ———— File.separator，java根据平台来返回 \ or /

                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));

                //2.通过反射机制获取对应的Class对象并加入到classSet
                Class targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });

        if (files != null) {  //对于foreach方法，做好判空，否则报nullException
            for (File f : files) {
                //递归调用
                extractClassFile(emptyClassSet, f, packageName);
            }
        }
    }


    /**
     * 获取Class对象
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     *实例化class
     *
     *@param clazz Class
     *@param <T> class的类型
     * @param accessible 是否支持创建出私有class对象的实例；让用户自己决定
     *@return 类的实例化
     */
    public static <T> T newInstance(Class<?> clazz, boolean accessible){
        try {
//            return (T) clazz.newInstance();   用这个方法好吗？ 这样不好,  这个方法过时了
            Constructor<?> constructor = clazz.getDeclaredConstructor(); // 获取无参构造函数，为了简单就只获取无参构造函数，没有 有参的构造函数
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e); //把异常记录在日志里
            throw new RuntimeException(e);  //运行时异常抛出
        }


    }


    /**
     * 获取classLoader    | classLoader能干啥————获取URL,得到URL里的path、protocol
     *
     * @return 当前ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 设置类的属性值
     *
     * @param field 成员变量
     * @param target 类实例
     * @param value 成员变量的值
     * @param accessible 是否允许设置私有属性
     */
    public static void setField(Field field, Object target, Object value, boolean accessible){
        field.setAccessible(accessible);
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            log.error("setField error", e);
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {  // 用于测试  extractPackageClass
        extractPackageClass("com.imooc.entity");
    }

}
