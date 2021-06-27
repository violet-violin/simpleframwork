package demo.pattern.factory.abstractf;

import demo.pattern.factory.entity.Keyboard;
import demo.pattern.factory.entity.LenovoMouse;
import demo.pattern.factory.entity.Mouse;
import demo.pattern.factory.method.LenovoKeyboard;

/**
 * @author malaka
 * @create 2020-11-29 22:01
 */
public class LenovoComputerFactory implements ComputerFactory {
    @Override
    public Mouse createMouse() {
        return new LenovoMouse();
    }

    @Override
    public Keyboard createKeyboard() {
        return new LenovoKeyboard();
    }
}
