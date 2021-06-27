package org.simpleframework.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求的方法参数名称
 */
@Target({ElementType.PARAMETER})  //作用于参数上
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    //方法参数名称
    String value() default "";
    //该参数是否是必须的————默认需要这个参数
    boolean required() default true;
}
