package org.prototex.event;

import com.google.common.collect.Maps;
import org.prototex.session.PrototexSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final Map<String, List<Event>> events = Maps.newConcurrentMap();

    public void on(String eventName, Event event) {
        this.events.computeIfAbsent(eventName, k -> new ArrayList<>());
        this.events.get(eventName).add(event);
    }

    public void emit(String eventName, PrototexSession session) {
        emit(eventName, session, null);
    }

    public void emit(String eventName, PrototexSession session, Object input) {
        List<Event> events = this.events.get(eventName);

        if (events != null) {
            events.forEach(event -> event.accept(session, input));
        }
    }

}
