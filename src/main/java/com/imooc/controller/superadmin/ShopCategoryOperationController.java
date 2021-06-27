package com.imooc.controller.superadmin;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.bo.ShopCategory;
import com.imooc.entity.dto.Result;
import com.imooc.service.solo.ShopCategoryService;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ShopCategory店铺类别 操作的Controller方法：增删改查
 * @author malaka
 * @create 2020-11-29 20:17
 */
@Controller
public class ShopCategoryOperationController {
    @Autowired
    private ShopCategoryService shopCategoryService;

    Result<Boolean> addShopCategory(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验  以及   页面请求参数转化
        return shopCategoryService.addShopCategory(new ShopCategory());
    }
    Result<Boolean> removeHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return shopCategoryService.removeShopCategory(1);
    }
    Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return shopCategoryService.modifyShopCategory(new ShopCategory());
    }
    Result<HeadLine> queryHeadLineById(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return  shopCategoryService.queryShopCategoryById(1);
    }
    Result<List<HeadLine>> queryHeadLine(HttpServletRequest req, HttpServletResponse resp){
        //TODO：参数校验以及请求参数转化
        return shopCategoryService.queryShopCategory(new ShopCategory(),1,100);
    }

}
