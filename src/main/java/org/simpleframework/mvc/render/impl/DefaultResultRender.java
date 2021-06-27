package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

/**
 * 默认渲染器：请求成功， 直接 往 resp 域设置 200 ，表示请求成功完成
 * @author malaka
 * @create 2020-12-21 15:03
 */
public class DefaultResultRender implements ResultRender {


    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        //请求状态码
        int code = requestProcessorChain.getResponseCode();
        //response域设置返回状态码
        requestProcessorChain.getResponse().setStatus(code);
    }
}
