package org.simpleframework.mvc.render;

import org.simpleframework.mvc.processor.RequestProcessorChain;

/**
 * 渲染请求结果，好像策略模式
 * @author malaka
 * @create 2020-12-21 15:00
 */
public interface ResultRender {
    // 执行渲染
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
