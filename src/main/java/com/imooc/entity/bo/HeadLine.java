package com.imooc.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.util.ClassUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

/**
 * @author malaka
 * @create 2020-11-28 21:31
 */
//@Getter@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadLine {

    //ClassUtil的测试，提取包下的类
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("***");
        Set<Class<?>> classSet = ClassUtil.extractPackageClass("com.imooc.entity");
        System.out.println(classSet);
        System.out.println("?????");
    }

    //主键ID
    private Long lineId;
    //头条名字
    private String lineName;
    //头条健接，点击头条会进入相应链接中
    private String lineLink;
    //头条图片
    private String lineImg;
    //权重，越大越排前显示
    private Integer priority;
    //0不可用1.可用
    private Integer enableStatus;
    //创建时间
    private Date createTime;
    //最近一次的更新时间
    private Date lastEditTime;
}
