package org.simpleframework.mvc;

import org.simpleframework.aop.AspectWeaver;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.DependencyInjector;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.processor.impl.ControllerRequestProcessor;
import org.simpleframework.mvc.processor.impl.JspRequestProcessor;
import org.simpleframework.mvc.processor.impl.PreRequestProcessor;
import org.simpleframework.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author malaka
 * @create 2020-11-29 19:46
 * <p>
 * 拦截所有的路由请求， @WebServlet("/")
 * 解析请求
 * 将请求派发给Controller的方法 去处理
 * /* 拦截所有请求
 */
/**
 * 拦截所有的路由请求， @WebServlet("/")
 * 解析请求
 * 将请求派发给Controller的方法 去处理
 * /* 拦截所有请求
 */

/**
 * 分发器
 */
@WebServlet("/*")
//配置成 /，可支持 RESTFUL风格，但会导致静态资源文件（jsp,css图片等）被拦截不能正常显示，但是可以通过配置来处理。推荐使用
//配置成 /*，可以请求到 controller中，但是跳转到jsp时会被拦截，不能渲染jsp视图，不使用，一般用于filter
// 就是 `/` 优先级较低，比如直接访问某个jsp页面时，不会交给当前servlet处理，而是给一个jsp的servlet处理。 既然是 框架，就要拦截所有请求。
public class DispatcherServlet extends HttpServlet {
    List<RequestProcessor> PROCESSOR = new ArrayList<>();
    @Override
    public void init() {
        //初始化常驻内存的应用
        //1.初始化容器和AOP容器
//        initIOC(false);;
        BeanContainer beanContainer = BeanContainer.getInstance();//创建容器单例
        beanContainer.loadBeans("com.imooc");//设置packageName
        new AspectWeaver().doAop();//aop织入
        new DependencyInjector().doIoc();//ioc依赖注入 // 要先aop，再Ioc织入，才能有完备的bean实例
        //2.初始化请求处理器责任链
        initProcessor();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.创建责任链对象实例 形参：RequestProcessor列表的迭代器、req、resp；后续的处理器进行请求处理时都会用到这3个关键信息；req得到请求相关信息，resp进行返回结果处理
        RequestProcessorChain requestProcessorChain = new RequestProcessorChain(PROCESSOR.iterator(), req, resp);
        //2.通过责任链模式依次调用请求处理器对请求进行处理，责任链依次处理请求
        requestProcessorChain.doRequestProcessorChain();
        //3.对处理结果进行渲染
        requestProcessorChain.doRender();
    }

    //初始化请求处理器责任链
    private void initProcessor() {
        PROCESSOR.add(new PreRequestProcessor());//对url进行预处理，方便后续适配controller //负责对请求的编码，对路径进行前置处理，是一个必须执行的处理器
        PROCESSOR.add(new StaticResourceRequestProcessor(getServletContext())); // todo getServletContext() 是啥？
        PROCESSOR.add(new JspRequestProcessor(getServletContext()));    //Jsp、Static两者顺序可对调。按mvc的处理流程，Controller在最后负责最后的处理
        PROCESSOR.add(new ControllerRequestProcessor()); //ControllerRequestProcessor放最后，会在责任链下最后执行，确保请求在没有被StaticResourceRequestProcessor和JspRequestProcessor 拦截的情况下，就在Controller处理器被处理
    }

    private void initIOC(boolean AOP) {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.fuyouj");
        if (AOP) {
            new AspectWeaver().doAop();
        }
        new DependencyInjector().doIoc();

        //加载ORM信息
//        TableContext.loadPoTables();
    }
}
