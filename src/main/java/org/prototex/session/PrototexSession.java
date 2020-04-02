package org.prototex.session;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.prototex.event.EventManager;

import java.util.Map;

@RequiredArgsConstructor
public class PrototexSession extends EventManager {

    private final Map<String, Object> attributes = Maps.newConcurrentMap();

    @Getter
    private final String id;

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

}
