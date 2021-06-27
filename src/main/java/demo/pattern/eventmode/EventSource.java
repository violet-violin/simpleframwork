package demo.pattern.eventmode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author malaka
 * @create 2020-12-08 22:09
 */
public class EventSource {
    private List<EventListener> listenerList = new ArrayList<>();

    public void register(EventListener listener){
        listenerList.add(listener);
    }

    public void publishEvent(Event event){
        for(EventListener listener : listenerList){
            listener.processEvent(event);
        }
    }

}
