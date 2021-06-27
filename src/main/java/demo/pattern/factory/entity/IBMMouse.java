package demo.pattern.factory.entity;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-09 18:52
 **/
public class IBMMouse implements Mouse {
    @Override
    public void sayHi() {
        System.out.println("我是IBM鼠标");
    }
}
