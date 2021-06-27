package demo.pattern.eventmode;

/**
 * @author malaka
 * @create 2020-12-08 22:07
 */
public class SingleClickEventListener implements EventListener {
    @Override
    public void processEvent(Event event) {
        if("singleclick".equals(event.getType()))
        System.out.println("one punch to death");
    }
}
