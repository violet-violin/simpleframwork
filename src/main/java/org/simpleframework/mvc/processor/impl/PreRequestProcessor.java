package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.RequestProcessorChain;

/**
 * 请求预处理，包括编码以及路径处理
 * 请求预处理 ，对请求进行统一的UTF-8编码 以及路径处理
 * 并对请求路径的最后斜杠进行去除操作 ——> 有些人写网址可能最后还要加个 / ，如 http://localhost:8080/simpleframework/
 * @author malaka
 * @create 2020-12-21 11:17
 */
@Slf4j
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //1.设置请求的编码 UTF-8
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");
        //2. 去除请求路径末尾的斜杠  方便后续适配Controller
        // 比如/aaa/bbb/ = /aaa/bbb
        String requestPath = requestProcessorChain.getRequestPath();
        if (requestPath.length() > 1 && requestPath.endsWith("/")){
            String substring = requestPath.substring(0, requestPath.length() - 1);
            requestProcessorChain.setRequestPath(substring);
        }
        log.info("preprocessor request {} {}",requestProcessorChain.getRequestMethod(),requestProcessorChain.getRequestPath());
        //交给 编译后续的处理器执行
        return true;
    }
}
