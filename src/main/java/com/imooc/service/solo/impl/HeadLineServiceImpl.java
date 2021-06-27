package com.imooc.service.solo.impl;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.dto.Result;
import com.imooc.service.solo.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author malaka
 * @create 2020-11-29 16:51
 */
@Slf4j
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    public Result<Boolean> addHeadLine(HeadLine headLine) {
        log.info("addHeadLine 执行了");
        log.info("addHeadLine 执行了,lineName[{}],lineLink[{}],lineImg[{}],priority[{}]",
                headLine.getLineName(),headLine.getLineLink(),headLine.getLineImg(),headLine.getPriority());
//        int i = 1/0;
        Result<Boolean> result = new Result<>();
        result.setCode(200);  //设置本次请求结果的状态码
        result.setMsg("请求成功");  //本次请求结果的详情
        result.setData(true);    //本次请求返回的结果集
        return result;
    }

    @Override
    public Result<Boolean> removeHeadLine(int headLineId) {
        return null;
    }

    @Override
    public Result<Boolean> modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<HeadLine> queryHeadLineById(int headLineId) {
        return null;
    }

    @Override
    public Result<List<HeadLine>> queryHeadLine(HeadLine headLineCondition, int pageIndex, int pageSize) {
        List<HeadLine> headLines = new ArrayList<>();
        HeadLine headLine = new HeadLine();
        headLine.setLineId(1L);
        headLine.setLineName("头条1");
        headLine.setLineLink("www.baidu.com");
        headLine.setLineImg("图片1的地址");
        headLines.add(headLine);
        HeadLine headLine2 = new HeadLine();
        headLine2.setLineId(2L);
        headLine2.setLineName("头条2");
        headLine2.setLineLink("www.baidu2.com");
        headLine2.setLineImg("图片2的地址");
        headLines.add(headLine2);
        Result<List<HeadLine>> result = new Result<>();
        result.setData(headLines);
        result.setCode(200);
        return result;
    }
}
