package com.imooc.controller.superadmin;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.dto.Result;
import com.imooc.service.solo.HeadLineService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.mvc.annotation.RequestMapping;
import org.simpleframework.mvc.annotation.RequestParam;
import org.simpleframework.mvc.annotation.ResponseBody;
import org.simpleframework.mvc.type.ModelAndView;
import org.simpleframework.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作头条的controller1方法：增删改查
 * @author malaka
 * @create 2020-11-29 20:10
 */
@Slf4j
@Controller
@RequestMapping(value = "/headline")
@ToString
public class HeadLineOperationController {

    @Autowired(value = "HeadLineServiceImpl")
    private HeadLineService headLineService;

    // 这个controller 处理前端传来的  添加头条 的任务
    @RequestMapping(value = "/add", method = RequestMethod.POST)//注意这里是POST
    public ModelAndView addHeadLine(@RequestParam("lineName") String lineName,
                                    @RequestParam("lineLink") String lineLink,
                                    @RequestParam("lineImg") String lineImg,
                                    @RequestParam("priority") Integer priority){//传入的参数@RequestParam("lineImg")与addheadline.jsp提交的参数name是一致的
        HeadLine headLine = new HeadLine();
        headLine.setLineName(lineName);
        headLine.setLineLink(lineLink);
        headLine.setLineImg(lineImg);
        headLine.setPriority(priority);
        Result<Boolean> result = headLineService.addHeadLine(headLine);
        ModelAndView modelAndView = new ModelAndView();
        // 这里只设置了 addheadline.jsp ，因为ViewResultRender 后续拼接了/templates/
        modelAndView.setView("addheadline.jsp").addViewData("result", result);
        return modelAndView;
    }

    public Result<Boolean> addHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验  以及   页面请求参数转化
//        log.info("addHeadLine 执行了");
//        System.out.println(this);
//        return null;
     return headLineService.addHeadLine(new HeadLine());
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)//测试mvc的渲染器、process
    public void removeHeadLine(){
        //TODO：参数校验以及请求参数转化
        System.out.println("删除HeadLine");
    }

    public Result<Boolean> removeHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return headLineService.removeHeadLine(1);
    }
    public Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return headLineService.modifyHeadLine(new HeadLine());
    }
    public Result<HeadLine> queryHeadLineById(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return  headLineService.queryHeadLineById(1);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)//测试jsonRender 渲染器
    @ResponseBody
    public Result<List<HeadLine>> queryHeadLine(){
        return headLineService.queryHeadLine(null,1, 100);
    }

    public Result<List<HeadLine>> queryHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return headLineService.queryHeadLine(null,1, 100);
    }
}


