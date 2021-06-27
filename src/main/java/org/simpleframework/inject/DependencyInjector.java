package org.simpleframework.inject;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author malaka
 * @create 2020-12-03 13:18
 */
@Slf4j
public class DependencyInjector {

    /**
     * Bean Container
     */
    private BeanContainer beanContainer;

    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * doIoc；this method，used to scan the fields marked by @Autowired, and inject对应的class的bean实例
     */
    public void doIoc() {
        if(ValidationUtil.isEmpty(beanContainer.getClasses())){
            log.warn("empty classSet in BeanContainer");
            return;
        }
        //1.遍历Bean容器中所有的class对象
        for(Class<?> clazz : beanContainer.getClasses()){
            //2.遍历Class对象的  **所有成员变量**
            Field[] fields = clazz.getDeclaredFields();
            if(ValidationUtil.isEmpty(fields)){
                continue;
            }
            for(Field field : fields ){
                //3.找出被Autowired标记的成员变量
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();  // 得到Autowired注解里的value值
                    //4.获取这些成员变量的类型，是一个Class对象
                    Class<?> fieldClass = field.getType();
                    //5.获取这些成员变量的类型在容器里对应的实例
                    Object fieldValue = getFieldInstance(fieldClass, autowiredValue);
                    if(fieldValue == null){
                        throw new RuntimeException("unable to inject relevant type, target fieldClass is: " + fieldClass.getName() + "autowiredValue is: " + autowiredValue );
                    }else {
                        //6.通过反射将对应的成员变量实例注入到  成员变量所在类的实例  里
                        Object targetBean = beanContainer.getBean(clazz);//这是传入一个 .class对象；得到容器内其bean实例
                        //给Bean实例targetBean   注入成员变量field  的值fieldValue  ； true ———— 是否支持访问私有属性
                        ClassUtil.setField(field,targetBean,fieldValue,true);
                }
                }
            }
        }

    }

    /**
     * 根据Class对象  在beanContainer里  获取其实例或者实现类
     * 举例：如MainPageController类里面有成员变量HeadLineShopCategoryCombineService（接口类型）；
     * 就要获取HeadLineShopCategoryCombineService实现类的实例
     * @param fieldClass
     * @return
     */
    private Object getFieldInstance(Class<?> fieldClass, String autowiredValue) {
        //什么时候注入的fieldClass的Bean实例？ ———— BeanContainer的loadBeans()方法运行后
        Object fieldValue = beanContainer.getBean(fieldClass);

        if(fieldValue != null){   //若fieldClass是接口的实现类，会标记上@Service，会被容器管理起来，这就 != null
            return fieldValue;
        }else {//若是接口，接口上没有标记上@Service，fieldValue == null；就去获取接口实现类的实例
            Class<?> inplementedClass = getImplementClass(fieldClass,autowiredValue);  //获得接口实现类的class对象
            if(inplementedClass != null){
                return beanContainer.getBean(inplementedClass);
            }else {
                return null;  // 接口实现类也没有 标上@Service
            }
        }
    }

    /**
     * 获取接口的实现类
     * @param fieldClass
     * @param autowiredValue
     * @return
     */
    private Class<?> getImplementClass(Class<?> fieldClass, String autowiredValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldClass);
        if(!ValidationUtil.isEmpty(classSet)){ // 即接口/父类有实现类
            if(ValidationUtil.isEmpty(autowiredValue)){
                if(classSet.size() == 1){
                    return classSet.iterator().next();
                }else { // 考虑接口有多个实现类的情况；spring有一个@Qualifier的注解在成员变量上标注出默认用哪个实现类
                    //  我们这里为了实现方便，就在Autowired注解里加个String value() default "";
                    //  public @interface Autowired {String value() default ""; }

                    //  如果多于两个实现类且用户未指定其中一个实现类 （autowiredValue为null），则抛出异常
                    throw new RuntimeException("multiple implemented classes for " +
                            fieldClass.getName() + "please set @Autowired's value to pick one");
                }
            }else { //Autowired注解有一个 autowiredValue 值，用户指定了 用哪个实现类
                for(Class<?> clazz : classSet) {
                    if(autowiredValue.equals(clazz.getSimpleName())){
                        return clazz;
                    }
                }
            }
        }
        return null;
    }


}
