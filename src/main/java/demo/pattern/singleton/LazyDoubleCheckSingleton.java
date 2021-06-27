package demo.pattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author malaka
 * @create 2020-12-01 21:13
 */
public class LazyDoubleCheckSingleton {

    //volatile  可以被异步线程修改
    private volatile static LazyDoubleCheckSingleton instance;

    private LazyDoubleCheckSingleton() {
    }

    public static LazyDoubleCheckSingleton getInstance() {
        //first check
        if (instance == null) {
            //同步  加锁
            synchronized ((LazyDoubleCheckSingleton.class)) {
                if (instance == null) {
                    //memory = allocate0;/1.分配对象内存空间
                    //instance(memory);//2.初始化对象
                    //instance = memory;//3.设置instance指向刚分配的内存地址，此时instance != null
                    //volatile 修饰的变量会严格按照1、2、3的顺序执行；不用volatile就可能指令重排序————1 3 2。
                    //在执行完3，未执行2时，若有另一个线程来getInstance()，可能就直接返回一个未初始化的instance对象。
                    instance = new LazyDoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(LazyDoubleCheckSingleton.getInstance());
        System.out.println(LazyDoubleCheckSingleton.getInstance());

        System.out.println("能用反射获取单例的对象吗");
        Class  clazz = LazyDoubleCheckSingleton.class;
        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        System.out.println(constructor.newInstance());  // 能
    }
}
