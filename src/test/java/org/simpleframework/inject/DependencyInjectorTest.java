package org.simpleframework.inject;

import com.imooc.controller.frontend.MainPageController;
//import org.junit.jupiter.api.Assertions;
import com.imooc.service.combine.impl.HeadLineShopCategoryCombineServiceImpl;
import com.imooc.service.combine.impl.HeadLineShopCategoryCombineServiceImpl2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;

/**
 * @author malaka
 * @create 2020-12-03 15:12
 */
public class DependencyInjectorTest {


    @DisplayName("依赖注入doIoc")
    @Test
    public void doIocTest(){
        BeanContainer beanContainer = BeanContainer.getInstance(); // 获取容器实例
        beanContainer.loadBeans("com.imooc"); // 加载
        Assertions.assertEquals(true, beanContainer.isLoaded());  // 确认已经加载
        MainPageController mainPageController = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, mainPageController instanceof MainPageController);
        Assertions.assertEquals(null, mainPageController.getHeadLineShopCategoryCombineService());
        new DependencyInjector().doIoc();  // 依赖注入
        Assertions.assertNotEquals(null, mainPageController.getHeadLineShopCategoryCombineService());

        // mainPageController 成员变量HeadLineShopCategoryCombineService 上的Autowired上value值标注的是HeadLineShopCategoryCombineServiceImpl
        Assertions.assertEquals(true, mainPageController.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl);
        Assertions.assertEquals(false, mainPageController.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl2);

    }
}
