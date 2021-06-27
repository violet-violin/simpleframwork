package demo.generic;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: codeBug
 * @createTime: 2020-06-07 22:06
 **/
public class GenericDemo {

    public static void main(String[] args) {

        List<String> linkedList = new LinkedList();
        linkedList.add("words");
//        linkedList.add(1);   // 不加<>编译没问题，遍历时才报错；加了就直接报编译问题
        linkedList.forEach(System.out::println);

    }

}
