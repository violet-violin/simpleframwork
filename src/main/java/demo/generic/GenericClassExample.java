package demo.generic;

import lombok.Data;

/**
 * @author malaka
 * @create 2020-11-28 21:46
 */
@Data
public class GenericClassExample<T> {
    //member这个成员变量的类型为T，T的类型由外部指定
    private T member;
    public GenericClassExample(T member){ //泛型构造方法形参membe的类型也为T，T的类型由外部指定
        this.member = member;
    }

    public T handleSomething(T target){
        return target;
    }


    public static <E> void printAray(E[] inputArray){

    }
}
