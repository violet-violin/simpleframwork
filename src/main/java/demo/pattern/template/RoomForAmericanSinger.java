package demo.pattern.template;

/**
 * @author malaka
 * @create 2020-12-05 10:51
 */
public class RoomForAmericanSinger extends KTVRoom{


    @Override
    protected void orderSong() {
        System.out.println("a english song!");
    }
}
