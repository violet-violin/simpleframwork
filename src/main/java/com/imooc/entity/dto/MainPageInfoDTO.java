package com.imooc.entity.dto;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

/**
 * @author malaka
 * @create 2020-11-29 16:55
 */
@Data
public class MainPageInfoDTO {

    /**
     * 头条列表
     */
    private List<HeadLine> headLineList;

    /**
     * 分类列表
     */
    private List<ShopCategory> shopCategoryList;
}
