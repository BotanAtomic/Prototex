package org.prototex.session;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.prototex.event.EventManager;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class PrototexSession extends EventManager {

    private final Map<String, Object> attributes = Maps.newConcurrentMap();

    @Getter
    private final Date connectionDate = new Date();

    @Getter
    private final Channel channel;

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public ChannelFuture send(Object message) {
        return channel.writeAndFlush(message);
    }

    public String getId() {
        return channel.id().asLongText();
    }
}
