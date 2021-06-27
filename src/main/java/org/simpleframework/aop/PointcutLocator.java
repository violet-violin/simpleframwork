package org.simpleframework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * 解析Aspect表达式并且定位被织入的目标
 * @author malaka
 * @create 2020-12-15 11:05
 */
public class PointcutLocator {  // 主要靠两个成员变量：pointcutParser、pointcutExpression
    /**
     * Pointcut解析器，直接给它赋值上Aspectj的所有表达式，以便支持对众多表达式的解析
     * 使用前要初始化其语法树，即装载可解析的语言，如解析  execution(* com.imoac.service..* . *(..))
     * getAllSupportedPointcutPrimitives代表支持AspectJ默认的所有语法树，但这里只用到execution、within的语法
     */
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );

    /**
     * 表达式解析器
     */
    private PointcutExpression pointcutExpression;

    // 形参expression 就是 Aspect 的pointcut值
    public PointcutLocator(String expression){ // 使用构造器 初始化 表达式解析器PointcutExpression
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression); // 解析string表达式得到对应的PointcutExpression实例
    }

    /**
     * 判断传入的Class对象是否是Aspect的目标代理类，即匹配Aspect注解的Pointcut表达式（初筛）
     * @param targetClass 目标类
     * @return 是否匹配
     */
    public boolean roughMatches(Class<?> targetClass){
        //couldMatchJoinPointsInType比较坑，只能检验within
        //不能校验（execution(精确到某个类除外)，call，get，set等表达式），面对无法校验的表达式，都会直接返回true
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断传入的Method实例对象是否是Aspect的目标代理方法，即匹配Pointcut表达式（精筛）
     * @param method
     * @return
     */
    public boolean accurateMatches(Method method){
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        if(shadowMatch.alwaysMatches()){ // shadowMath，有完全匹配、部分匹配这些方法；只有完全匹配才返回true
            return true;
        }else{
            return false;
        }
    }
}
