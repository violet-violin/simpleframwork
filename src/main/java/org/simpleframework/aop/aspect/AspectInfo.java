package org.simpleframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.simpleframework.aop.PointcutLocator;

/**
 * @author malaka
 * @create 2020-12-14 15:05
 */
@AllArgsConstructor
@Getter
public class AspectInfo {
    private int orderIndex;       // 该变量存储Order注解的值
    // DefaultAspect抽象类(用了模板模式，写了3个钩子方法before、afterReturning、afterThrowing；
    // 并且所有的切面必须继承自该类)
    private DefaultAspect aspectObject;
    //用于 解析Aspect表达式 并且 定位被织入的目标；     // aop 2.0
    private PointcutLocator pointcutLocator;
}
