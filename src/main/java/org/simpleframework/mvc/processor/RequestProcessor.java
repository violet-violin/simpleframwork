package org.simpleframework.mvc.processor;

/**
 请求执行器——请求处理器的鼻祖
 * @author malaka
 * @create 2020-12-21 11:12
 */
public interface RequestProcessor {
    /**
     *
     * @param requestProcessorChain 以责任链的模式 处理请求——依次调用RequestProcessor接口实现类的process方法
     * @return false终止责任链模式，true下一个责任链的来执行
     * @throws Exception
     */
    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;

}
