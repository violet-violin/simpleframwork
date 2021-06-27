package com.imooc.service.combine;

import com.imooc.entity.dto.MainPageInfoDTO;
import com.imooc.entity.dto.Result;

/**
 * @author malaka
 * @create 2020-11-29 16:53
 */
// 返回 ——> HeadLine、ShopCategory两者的结合的Result ———— 定义这个方法
public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDTO> getMainPageInfo();  // MainPageInfoDTO 组合了 List<HeadLine>、List<ShopCategory>
}
