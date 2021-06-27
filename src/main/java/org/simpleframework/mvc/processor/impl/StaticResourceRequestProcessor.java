package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.RequestProcessorChain;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 静态资源请求处理，包括但不限于图片、css、以及js文件等
 * 负责对静态资源请求的处理————遇到静态资源 转发给tomcat的   defaultServlet
 * @author malaka
 * @create 2020-12-21 11:19
 */
@Slf4j
public class StaticResourceRequestProcessor implements RequestProcessor {
    //tomcat默认请求派发器RequestDispatcher的名称
    private RequestDispatcher defaultDispatcher;
    private final String Default_TOMCAT_SERVLET = "default";
    private final String STATIC_RESOURCE_PREFIX = "/static/";

    public StaticResourceRequestProcessor(ServletContext servletContext) {
        //使用默认的处理器
        this.defaultDispatcher = servletContext.getNamedDispatcher(Default_TOMCAT_SERVLET);
        if (this.defaultDispatcher == null){
            throw new RuntimeException("There is no default tomcat servlet, 获取Tomcat默认servlet失败");
        }
        log.info("The default servlet for static resource is {}", Default_TOMCAT_SERVLET);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //1.通过请求路径判断是否是请求的静态资源webapp/static
         if (isStaticResource(requestProcessorChain.getRequestPath()) == true){
            //2.如果是静态资源，则将请求转发给default servlet处理
            defaultDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            //静态资源请求  不需要后序处理
            return false;
        }
        return true;
    }

    /**
     * 通过请求路径前缀（目录）是否为静态资源/static/
     * @param requestPath
     * @return
     */
    private boolean isStaticResource(String requestPath) {
        return requestPath.startsWith(STATIC_RESOURCE_PREFIX);
    }


}
