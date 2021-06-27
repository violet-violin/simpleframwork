package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 页面渲染器，解析modelAndView；会解析程序里设置好的model 和 view，渲染到对应的页面里
 * @author malaka
 * @create 2020-12-21 15:03
 */
public class ViewResultRender implements ResultRender {

    public static final String VIEW_PATH = "templates";
    private ModelAndView modelAndView;

    public ViewResultRender(Object mv) {
        //1.如果入参类型是ModelAndView，则直接赋值给成员变量
        if (mv instanceof ModelAndView){
            this.modelAndView = (ModelAndView) mv;
        }//2，如果入参类型是String，则为视图的路径()，需要包装后才赋值给成员变量
        // mv 是啥，就看 controller方法 处理后的结果是啥；可能 是 ModelAndView类型，也可能是String
        // RequestProcessorChain#setResultRender()里的View渲染器传入的就是请求处理结果
        else if (mv instanceof String){
            this.modelAndView = new ModelAndView().setView((String) mv);
        }else {//3.针对controller处理结果的其他情况，则直接抛出异常
            throw new RuntimeException("illegal request result type (请求类型错误)");
        }
    }

    /**
     * 将请求处理结果  按照视图路径  转发至对应视图进行展示
     * @param requestProcessorChain
     * @throws Exception
     */
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            //将controller方法处理后的请求结果 设置进req域；  关键是modelAndView的数据从哪儿来的 ——> 构造函数
            request.setAttribute(entry.getKey(),entry.getValue());
        }
        //支持jsp视图，jsp规定保存在templates包下, 转发到 对应 path 的jsp 页面
        request.getRequestDispatcher("/" + VIEW_PATH + "/" +path).forward(request,response);
    }
}
