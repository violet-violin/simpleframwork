package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 待执行的Controller及其方法实例和参数的映射
 * @Desc 封装待处理的Controller及其方法实例和参数的映射
 * @author malaka
 * @create 2020-12-21 16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    //controller对应的class对象，用来保存controller的class对象
    private Class<?> controllerClass;
    //执行的controller方法实例
    private Method invokeMethod;
    //用来保存方法参数名称及其对应的参数类型
    private Map<String,Class<?>> methodParameters;
}
