package demo.pattern.proxy.impl;

/**
 * @author malaka
 * @create 2020-12-14 13:31
 */
//cglib不需要接口实现类实现任何接口，直接用一个类就可以了；jdk的动态代理就需要
public class CommonPayment {
    public void pay(){
        System.out.println("个人名义或者公司名义都可以走这个支付通道");
    }
}
