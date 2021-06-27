package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.core.annotation.Component;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.core.annotation.Repository;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author malaka
 * @create 2020-12-01 22:10
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)//私有构造函数
public class BeanContainer {


    /**
     * 存放所有被配置标记的目标对象的Map
     * ConcurrentHashMap  支持高并发，1.8后摒弃了分段锁，使用了cas、红黑树
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap();


    /**
     * 加载bean的注解列表
     * 配置的管理与获取  ,  常量，存储四个.class对象，只能存储标签；后面aop又加了个Aspect.class
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Service.class, Repository.class, Aspect.class);


    /**
     * 获取Bean容器实例   |  单例模式 ———— 线程安全的、能抵御反射和序列化入的单例容器 BeanContainer
     *
     * @return BeanContainer
     */
    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;
        private BeanContainer instance;

        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    /**
     * 容器是否已经加载过bean，如果已经加载过，就别再加载了
     */
    private boolean loaded = false;

    /**
     * 容器是否已加载过Bean，如果已经加载过，就别再加载了
     *
     * @return 是否已加载
     */
    public boolean isLoaded() {
        return loaded;
    }


    /**
     * 扫描加载所有Bean
     * <p>
     * synchronized: 避免多个线程扫描加载容器
     *
     * @param packageName 包名
     */
    public synchronized void loadBeans(String packageName) { // synchronized ———— 防止多个线程 使用loadBeans()方法

        //判断bean容器是否被加载过，如果已经加载过，就别再加载了
        if (isLoaded()) {
            log.warn("BeanContainer has been loaded.");
            return;
        }

        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("extract nothing from packageName" + packageName);
            return;
        }
        // 遍历每一个packageName下的 class 对象，看它是否有BEAN_ANNOTATION里的4个标签之一
        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                //如果类上面标记了定义的注解,就把它扫描进容器里
                if (clazz.isAnnotationPresent(annotation)) {
                    //将目标类本身作为键，目标类的实例作为值，放入到beanMap中  ； ClassUtil.newInstance方法获取的都是无参构造器的反射对象
                    beanMap.put(clazz, ClassUtil.newInstance(clazz, true));
                }
            }
        }
        loaded = true; // 设置 容器已经加载过bean
    }

    /**
     * Bean 实例的数量
     *
     * @return 数量
     */
    public int size() {
        return beanMap.size();
    }

//////////////////////////////////////////////////容器的一些操作

    /**
     * 添加/更新一个class对象及其Bean实例
     *
     * @param clazz Class对象
     * @param bean  Bean对象
     * @return 原有的Bean实例（因为addBean方法的功能是添加/更新），没有则返回null
     * 若先没有这个clazz，岂不add进去后会返回null，这个方法含义就这样
     */
    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }

    /**
     * 移除一个IOC容器管理的对象
     *
     * @param clazz Class对象
     * @return 删除的Bean实例，没有则返回null
     */
    public Object removeBean(Class<?> clazz) {
        return beanMap.remove(clazz);
    }

    /**
     * 根据Class对象获取Bean实例
     *
     * @param clazz Class对象
     * @return Bean实例
     */
    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    /**
     * 获取容器管理的所有Class对象集合
     *
     * @return Class集合
     */
    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    /**
     * 获取所有Bean集合
     *
     * @return Bean集合
     */
    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }


    /**
     * 根据传入的注解   筛选出Bean的Class集合；如可以给出所有标记@Controller注解的类的Class对象
     *
     * @param annotation 注解;    这个annotation一般是PersonInfoAnnotation.class，这个是注解的 .class对象
     * @return Class集合
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        //1. 获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        //2. 通过注解筛选被注解标记的class对象，并添加到classSet里
        HashSet<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            //类是否有相关的注解标记；把有注解的Class对象给add
            if (clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0 ? classSet : null;
    }

    /**
     * 通过接口或者父类获取实现类或者子类的Class集合，不包括其（接口/父类）本身
     *
     * @param interfaceOrClass 接口Class或者父类Class
     * @return Class集合
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        //1. 获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        //2. 判断keySet里的元素是否是传入的接口或者类的子类；如果是，就将其添加到classSet里
        HashSet<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            //判断keySet里的元素是否是传入的接口或者类的子类  && 非本身
            if (interfaceOrClass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrClass)) {
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0 ? classSet : null;
    }




//    public static void main(String[] args) {
//        System.out.println(BEAN_ANNOTATION);
//    }


}
