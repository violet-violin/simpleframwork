package demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author malaka
 * @create 2020-12-03 10:40
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)

public @interface TestAnnotation {

}

