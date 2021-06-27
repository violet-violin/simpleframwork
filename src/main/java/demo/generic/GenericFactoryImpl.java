package demo.generic;

/**
 * @author malaka
 * @create 2020-11-29 16:22
 */
public class GenericFactoryImpl<T,N> implements GenericIFactory<T,N> {
    @Override
    public T nextObject() {
        return null;
    }

    @Override
    public N nextNumber() {
        return null;
    }
}
