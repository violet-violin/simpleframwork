package demo.pattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author malaka
 * @create 2020-12-01 21:34
 */
public class EnumStarvingSingleton {

    private EnumStarvingSingleton(){}

    public static EnumStarvingSingleton getInstance(){
        return ContainerHolder.HOLDER.instance;  // HOLDER 是枚举类 EnumStarvingSingleton 的对象，这里是对象调用成员变量。
    }

    private enum ContainerHolder{
        HOLDER;

        private EnumStarvingSingleton instance;

        ContainerHolder(){ // 这是无参构造函数；  枚举的构造函数本就是private的, 这里没写；
            instance = new EnumStarvingSingleton();
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(EnumStarvingSingleton.getInstance()); // 单例模式输出

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Class<EnumStarvingSingleton> clazz = EnumStarvingSingleton.class;
        Constructor<EnumStarvingSingleton> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        EnumStarvingSingleton enumStarvingSingleton = (EnumStarvingSingleton) constructor.newInstance();
        System.out.println(enumStarvingSingleton.getInstance());  // 通过反射new对象
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //Constructor<ContainerHolder> constructor1 = ContainerHolder.class.getDeclaredConstructor(); // 获取枚举的无参构造函数
        // 获取枚举的有参构造函数———— protected Enum(String name, int ordinal) {  // Enum.java里的
        Constructor<ContainerHolder> constructor1 = ContainerHolder.class.getDeclaredConstructor(String.class, int.class);
        constructor1.setAccessible(true);
        System.out.println(EnumStarvingSingleton.getInstance());

        //java.lang.IllegalArgumentException: Cannot reflectively create enum objects
        System.out.println(constructor1.newInstance());  //java.lang.NoSuchMethodException ———— 报错，可见 枚举防止了反射的攻击
    }
}
