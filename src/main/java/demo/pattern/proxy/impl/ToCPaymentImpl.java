package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToCPayment;

/**
 * @author malaka
 * @create 2020-12-14 10:38
 */
public class ToCPaymentImpl implements ToCPayment {
    @Override
    public void pay(){ // 用户（被代理类）只负责付钱，背后支付宝（代理类）负责背后从银行取钱、付钱给商家
        System.out.println("以用户的名义进行支付");
    }
}

