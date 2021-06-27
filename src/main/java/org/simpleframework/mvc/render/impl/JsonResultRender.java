package org.simpleframework.mvc.render.impl;

import com.google.gson.Gson;
import org.simpleframework.mvc.processor.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

import java.io.PrintWriter;

/**
 * Json渲染器
 * Json渲染器
 * @author malaka
 * @create 2020-12-21 15:03
 */
public class JsonResultRender implements ResultRender {


    private Object JsonData;;
    public JsonResultRender(Object res) {
        this.JsonData = res;
    }
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        //设置响应头；这是啥？    setContentType()  ??
        requestProcessorChain.getResponse().setContentType("application/json");
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");
        //响应流写入经过gson格式化之后的处理结果
        try(PrintWriter writer = requestProcessorChain.getResponse().getWriter()) {//加个try啥意思？？
            Gson gson = new Gson();
            String json = gson.toJson(JsonData);//结果转为json格式
            writer.write(json);
            writer.flush();
        }
    }



}
