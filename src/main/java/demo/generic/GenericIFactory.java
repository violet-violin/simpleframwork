package demo.generic;

/**
 * @author malaka
 * @create 2020-11-29 16:17
 */
public interface GenericIFactory<T,N> {
    T nextObject();
    N nextNumber();
}
