package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * 内部异常渲染器
 * @author malaka
 * @create 2020-12-21 15:03
 */
public class InternalErrorResultRender implements ResultRender {

    private String errorMsg;
    public InternalErrorResultRender(String errorMsg){
        this.errorMsg = errorMsg;
    }
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        //给resp传入状态码500、errorMsg
        // errorMsg，是RequestProcessorChain#doRequestProcessorChain() catch异常时，new InternalErrorResultRender(e.getMessage()); 传入的
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
    }
}
