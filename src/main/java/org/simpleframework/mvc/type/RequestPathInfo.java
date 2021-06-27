package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装类：用于封装存储HttpServletRequest里提取出来的请求路径和请求方法
 * @author malaka
 * @create 2020-12-21 20:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPathInfo {
    // http请求方法 get/post
    private String httpMethod;
    //请求路径
    private String httpPath;
}
