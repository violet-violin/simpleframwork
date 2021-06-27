package demo.pattern.factory.simple;

import demo.pattern.factory.entity.DellMouse;
import demo.pattern.factory.entity.HpMouse;
import demo.pattern.factory.entity.LenovoMouse;
import demo.pattern.factory.entity.Mouse;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author:
 * @createTime: 2020-06-09 18:30
 **/
public class MouseFactory {

    public static Mouse createMouse(int type) {
        switch (type) {
            case 0 :
                return new DellMouse();
            case 1 :
                return new HpMouse();
            case 2 :
                return new LenovoMouse();
            default :
                return new DellMouse();
        }
    }


    public static void main(String[] args) {
        Mouse mouse = MouseFactory.createMouse(2);
        mouse.sayHi();
    }
}
