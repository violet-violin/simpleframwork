package org.simpleframework.mvc.processor.impl;

import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.RequestProcessorChain;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * jsp资源请求处理————负责对不经过控制器转发的JSP页面的处理
 * @author malaka
 * @create 2020-12-21 11:17
 */
public class JspRequestProcessor implements RequestProcessor {
    //jsp请求的RequestDispatcher的名称
    private static final String JSP_SERVLET = "jsp";
    //Jsp请求资源路径前缀——设置为templates
    private static final String JSP_RESOURCE_PREFIX = "/templates/";

    //Jsp的RequestDispatcher，处理jsp资源
    private RequestDispatcher jspServlet;

    public JspRequestProcessor(ServletContext servletContext) {  // ServletContext 是啥 东西 ? DispatcherServlet 中使用该类是，传入了一个getServletContext()
        jspServlet = servletContext.getNamedDispatcher(JSP_SERVLET);  // 换了一个Servlet来处理，即jspServlet
        if (jspServlet == null){
            throw new RuntimeException("没有找到jsp servlet");
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        if (isJspResource(requestProcessorChain.getRequestPath())){
            jspServlet.forward(requestProcessorChain.getRequest(),requestProcessorChain.getResponse());
            //jsp请求处理后，不再进行后序处理；后续的processor就不用管了
            return false;
        }
        //交给后续处理，即ControllerRequestProcessor来处理
        return true;
    }

    /**
     * 判断是否是jsp资源
     * @param url
     * @return
     */
    private boolean isJspResource(String url) {
        return url.startsWith(JSP_RESOURCE_PREFIX);
    }
}
