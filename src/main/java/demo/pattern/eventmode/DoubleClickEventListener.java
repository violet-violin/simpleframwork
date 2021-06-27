package demo.pattern.eventmode;

/**
 * @author malaka
 * @create 2020-12-08 22:07
 */
public class DoubleClickEventListener implements EventListener {
    @Override
    public void processEvent(Event event) {
        if("doubleclick".equals(event.getType()))
        System.out.println("two punches to death");
    }
}
