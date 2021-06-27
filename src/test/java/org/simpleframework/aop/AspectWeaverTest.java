package org.simpleframework.aop;

import com.imooc.controller.superadmin.HeadLineOperationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.DependencyInjector;

/**
 * @author malaka
 * @create 2020-12-14 21:10
 */
public class AspectWeaverTest {
    @DisplayName("织入通用逻辑测试：doAop")
    @Test
    public void doAopTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();//创建容器的单例
        beanContainer.loadBeans("com.imooc");//指定packageName

        // 是织入比依赖注入先完成，先织入了再对Bean做其他的。
        // 因为容器里还有用代理对象替代 被代理对象的操作；先用cglib给容器进行动态织入，再依赖注入，容器才有完备的bean实例
        new AspectWeaver().doAop();//织入
        new DependencyInjector().doIoc();//依赖注入

        HeadLineOperationController headLineOperationController = (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);
        headLineOperationController.addHeadLine(null, null);
    }
}
