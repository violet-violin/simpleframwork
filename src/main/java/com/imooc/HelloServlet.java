package com.imooc;

import com.imooc.entity.bo.HeadLine;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 配置成 /，可支持 RESTFUL风格，但会导致静态资源文件（jsp,css图片等）被拦截不能正常显示，但是可以通过配置来处理。推荐使用

// 配置成 /*，可以请求到 controller中，但是跳转到jsp时会被拦截，不能渲染jsp视图，不使用，一般用于filter
// /* 拦截了所有请求，req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req,resp); 这句请求转发也会被拦截，然后又进入service方法，service又进入doGet方法，就死循环了。
@Slf4j  //省去创建log
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("初始化Servlet（initializing Servlet）");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("it's me to execute the doGet method, i'm the entrance");
        doGet(req,resp);
    }

    //    Logger log = LoggerFactory.getLogger(HelloServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);

        String name = "my simple framework(我的简易框架)";
        log.debug("name is " + name);
        req.setAttribute("name", name);
        req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req,resp);

        HeadLine headLine = new HeadLine();
        headLine.setLineId(1L);
        headLine.getLineId();
    }

    @Override
    public void destroy() {
        System.out.println("Destroy...");
    }
}
