package org.simpleframework.mvc.annotation;


import org.simpleframework.mvc.type.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识Controller的方法与请求路径和请求方法的映射关系
 */
@Target({ElementType.TYPE,ElementType.METHOD})   //用在类上和方法上
@Retention(RetentionPolicy.RUNTIME)   //在程序运行时获取注解信息
public @interface RequestMapping {
    //请求路径
    String value() default "";

    //请求方法：为了实现上的简单，只支持GET、POST
    RequestMethod method() default RequestMethod.GET;
}
