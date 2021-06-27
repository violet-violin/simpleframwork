package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToBPayment;
import demo.pattern.proxy.ToCPayment;

/**
 * @author malaka
 * @create 2020-12-14 10:44
 */
public class AlipayToB implements ToBPayment {
    ToBPayment toBPayment;

    public AlipayToB(ToBPayment toBPayment){
        this.toBPayment = toBPayment;
    }

    @Override
    public void pay() {
        beforePay();
        toBPayment.pay();
        afterPay();
    }

    private void beforePay(){
        System.out.println("从招行取款");
    }
    private void afterPay(){
        System.out.println("支付给慕课网");
    }
}
