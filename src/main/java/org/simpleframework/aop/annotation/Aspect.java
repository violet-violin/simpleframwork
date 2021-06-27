package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

/**
 * @author malaka
 * @create 2020-12-14 14:38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

//    /**  aop1.0
//     * 需要被织入横切逻辑的注解标签
//     * 如果value = controller，那么controller标记的类就会被织入，
//     * Class类型的value，表示只能用注解类型的class为value赋值，如Controller.class、Service.class等等
//     * @return
//     */
//    Class<? extends Annotation> value();


    //aop2.0改动，引入了aspectj
    //表示要切的范围

    // 不使用AspectJ时支持的  joinpoint
    Class<? extends Annotation> value() default DefaultAspect.class;

    // AspectJ支持的横切表达式
    String pointcut() default  "";
    //"execution(*com.imooc.controller.frontend..*.*(..))" 及 within(com.imooc.controller.frontend.*) 这两个表达式 都是织入同一个目标
}
