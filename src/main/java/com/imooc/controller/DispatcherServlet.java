package com.imooc.controller;

import com.imooc.controller.frontend.MainPageController;
import com.imooc.controller.superadmin.HeadLineOperationController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author malaka
 * @create 2020-11-29 19:46
 */

// 以HelloServlet为例——
// 配置成 /，可支持 RESTFUL风格，但会导致静态资源文件（jsp,css图片等）被拦截不能正常显示，但是可以通过配置来处理。推荐使用

// 配置成 /*，可以请求到 controller中，但是跳转到jsp时会被拦截，不能渲染jsp视图，不使用，一般用于filter
// `/*` 拦截了所有请求，req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req,resp); 这句请求转发也会被拦截，然后又进入service方法，service又进入doGet方法，就死循环了。
// 就是 `/` 优先级较低，比如直接访问某个jsp页面时，不会交给当前servlet处理，而是给一个jsp的servlet处理。
@WebServlet("/")    //该servlet拦截所有请求
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(){ // 用于 首次请求前的初始化
        System.out.println("我是首次请求被处理前执行的，后续不会再执行");
    }

    // service 方法 就是用于处理请求的
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //根据url地址与请求方法来进行派发的思路是可行的
        System.out.println("request path is: " + req.getServletPath());  // 如 /frontend/getmainpageinfo
        System.out.println("request method is: " + req.getMethod());   // 如GET、POST...
        if(req.getServletPath() == "/frontend/getmainpageinfo" && req.getMethod() =="GET"){
            new MainPageController().getMainPageInfo(req, resp);
        }else if(req.getServletPath() == "/superadmin/addheadline" && req.getMethod() == "POST"){
            new HeadLineOperationController().addHeadLine(req,resp);
        }
    }
}
