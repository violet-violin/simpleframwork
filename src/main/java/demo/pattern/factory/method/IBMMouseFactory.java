package demo.pattern.factory.method;

import demo.pattern.factory.entity.IBMMouse;
import demo.pattern.factory.entity.Mouse;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 18:53
 **/
public class IBMMouseFactory extends LenovoMouseFactory {

    @Override
    public Mouse createMouse() {
        return new IBMMouse();
    }
}
