package demo.generic;

/**
 * @author malaka
 * @create 2020-11-29 16:18
 */
public class RobertFactory implements GenericIFactory<String, Integer> {

    @Override
    public String nextObject() {
        return null;
    }

    @Override
    public Integer nextNumber() {
        return null;
    }
}
