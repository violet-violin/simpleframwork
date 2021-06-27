package demo.pattern.eventmode;

/**
 * @author malaka
 * @create 2020-12-08 22:13
 */
public class EventModeDemo {

    public static void main(String[] args) {
        //事件源
        EventSource eventSource = new EventSource();
        //两个事件监听器
        SingleClickEventListener singleClickEventListener = new SingleClickEventListener();
        DoubleClickEventListener doubleClickEventListener = new DoubleClickEventListener();

        //事件
        Event event = new Event();
        event.setType("singleclick");
        //事件源注册监听器
        eventSource.register(singleClickEventListener);
        eventSource.register(doubleClickEventListener);
        //事件源发布事件
        eventSource.publishEvent(event);


    }
}
