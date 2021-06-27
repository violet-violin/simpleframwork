package demo.pattern.template;

/**
 * @author malaka
 * @create 2020-12-05 10:51
 */
public class RoomForChineseSinger extends KTVRoom{
    @Override
    protected void orderSong() {
        System.out.println("来首中文歌");
    }

    @Override
    protected void orderExtra() {
        System.out.println("东西便宜，一样来一份");
    }
}
