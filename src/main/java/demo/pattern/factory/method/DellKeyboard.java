package demo.pattern.factory.method;

import demo.pattern.factory.entity.Keyboard;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 18:59
 **/
public class DellKeyboard implements Keyboard {
    @Override
    public void sayHello() {
        System.out.println("我是戴尔键盘");
    }
}
