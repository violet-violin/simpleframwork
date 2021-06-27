package org.simpleframework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author malaka
 * @create 2020-12-14 14:39
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    //控制切面类的执行顺序，按照order的value值执行，越小越先执行。
    // 因为同一类（如多个controller），可能有多个Aspect注解
    int value();
}
