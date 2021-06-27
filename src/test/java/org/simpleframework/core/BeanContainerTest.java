package org.simpleframework.core;

import com.imooc.controller.DispatcherServlet;
import com.imooc.controller.frontend.MainPageController;
import com.imooc.service.solo.HeadLineService;
import com.imooc.service.solo.impl.HeadLineServiceImpl;
import org.junit.jupiter.api.*;
import org.simpleframework.core.annotation.Controller;

import java.util.Set;

/**
 * @author malaka
 * @create 2020-12-02 21:57
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 控制测试方法的顺序，配合@Order(1) ...
public class BeanContainerTest {

    private static BeanContainer beanContainer;

    @BeforeAll  //这个注解代表只执行这个方法一次
    static void init(){
        beanContainer = BeanContainer.getInstance();   // 得到一个单例的容器
    }

    @DisplayName("加载目标类及其实例到BeanContainer：loadBeansTest")
    @Order(1)
    @Test
    public void loadBeansTest(){
        Assertions.assertEquals(false, beanContainer.isLoaded());
        Set<Class<?>> classes = beanContainer.getClasses();
        beanContainer.loadBeans("com.imooc");
//        for(Class c: classes){
//            System.out.println(c + "__");
//        }
//        System.out.println(beanContainer.getClasses());
        Assertions.assertEquals(11, beanContainer.size());
        Assertions.assertEquals(true, beanContainer.isLoaded());
    }

    @DisplayName("根据类获取其实例：getBeanTest")
    @Order(2)
    @Test
    public void getBeanTest(){
//        beanContainer.loadBeans("com.imooc");  // 为什么我要加上这句才能test通过，以前就不用
        MainPageController controller = (MainPageController) beanContainer.getBean(MainPageController.class);
//        System.out.println(controller);
        Assertions.assertEquals(true, controller instanceof MainPageController);
        DispatcherServlet dispatcherServlet =(DispatcherServlet) beanContainer.getBean(DispatcherServlet.class);
        Assertions.assertEquals(null, dispatcherServlet);
    }

    @DisplayName("根据注解获取对应的实例: getClassesByAnnotationTest")
    @Order(3)
    @Test
    public void getClassesByAnnotationTest(){
//        beanContainer.loadBeans("com.imooc");  // 为什么我要加上这句才能test通过，以前就不用
        Assertions.assertEquals(true,beanContainer.isLoaded());
        Assertions.assertEquals(3, beanContainer.getClassesByAnnotation(Controller.class).size());
    }

    @DisplayName("根据接口获取实现类：getClassesBySuperTest")
    @Order(4)
    @Test
    public void getClassesBySuperTest(){
//        beanContainer.loadBeans("com.imooc");  // 为什么我要加上这句才能test通过，以前就不用
        Assertions.assertEquals(true,beanContainer.isLoaded());
        Assertions.assertEquals(true, beanContainer.getClassesBySuper(HeadLineService.class).contains(HeadLineServiceImpl.class));
    }



}
