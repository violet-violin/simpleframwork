package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToBPayment;
import demo.pattern.proxy.ToCPayment;

/**
 * @author malaka
 * @create 2020-12-14 10:38
 */
public class ToBPaymentImpl implements ToBPayment {
    @Override
    public void pay(){
        System.out.println("以公司的名义进行支付");
    }
}
