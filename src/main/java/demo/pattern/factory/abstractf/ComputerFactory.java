package demo.pattern.factory.abstractf;

import demo.pattern.factory.entity.Keyboard;
import demo.pattern.factory.entity.Mouse;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 19:01
 **/
public interface ComputerFactory {

    Mouse createMouse();

    Keyboard createKeyboard();

}
