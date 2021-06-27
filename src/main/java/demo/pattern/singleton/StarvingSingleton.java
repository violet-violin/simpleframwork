package demo.pattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author malaka
 * @create 2020-12-01 21:08
 */
public class StarvingSingleton {

    private static final StarvingSingleton starvingSingleton = new StarvingSingleton();

    private StarvingSingleton() {
    }


    public static StarvingSingleton getInstance() {
        return starvingSingleton;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(StarvingSingleton.getInstance());
        System.out.println(StarvingSingleton.getInstance());
        System.out.println(StarvingSingleton.getInstance() == StarvingSingleton.getInstance());

        System.out.println("能用反射获取单例的对象吗");
        Class  clazz = StarvingSingleton.class;
        Constructor constructor = clazz.getDeclaredConstructor(); // 获取到无参构造函数的class对象
        constructor.setAccessible(true);
        System.out.println(constructor.newInstance());  // 能，对比StarvingSingleton.getInstance()，生成的实例是另一个了。
    }
}
