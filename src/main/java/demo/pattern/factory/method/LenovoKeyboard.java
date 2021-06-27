package demo.pattern.factory.method;

import demo.pattern.factory.entity.Keyboard;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 18:59
 **/
public class LenovoKeyboard implements Keyboard {
    @Override
    public void sayHello() {
        System.out.println("我是Lenovo键盘");
    }
}
