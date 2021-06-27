package demo.pattern.template;

import java.sql.PseudoColumnUsage;

/**
 * @author malaka
 * @create 2020-12-05 10:51
 */
public class TemplateDemo {
    public static void main(String[] args) {
        RoomForChineseSinger room1 = new RoomForChineseSinger();
        room1.procedure();
        RoomForAmericanSinger room2 = new RoomForAmericanSinger();
        room2.procedure();
    }
}
