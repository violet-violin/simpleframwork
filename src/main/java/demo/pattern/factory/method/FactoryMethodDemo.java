package demo.pattern.factory.method;

import demo.pattern.factory.entity.Mouse;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 18:48
 **/
public class FactoryMethodDemo {

    public static void main(String[] args) {

        MouseFactory mf = new HpMouseFactory();
        Mouse mouse = mf.createMouse();
        mouse.sayHi();

    }

}
