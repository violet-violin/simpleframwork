package org.simpleframework.mvc.type;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

/**
 * 存储处理完后的结果数据  以及显示该数据的视图
 * @author malaka
 * @create 2020-12-22 15:06
 */
public class ModelAndView {
    //页面所在的路径，页面路径
    @Getter
    private String view;
    //页面的data数据————存储 请求结果里 的 数据名字 和 对应的值
    @Getter
    private Map<String,Object> model = new HashMap<>();

    public ModelAndView setView(String view){
        this.view = view;
        return this; // 返回当前对象，可以 链式的调用，很方便
    }
    //modelAndView.setView("addheadine.jsp").addViewData("aaa", "bbb");  能通过一连串的点号 (.) 进行ModelAndView的设置
    public ModelAndView addViewData(String attributeName,Object attributeValue ){ // 给 model 赋值
        model.put(attributeName,attributeValue);
        return this;
    }
}