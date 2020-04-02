package org.prototex.event;

import org.prototex.session.PrototexSession;

public interface Event {

    void accept(PrototexSession session, Object input);

}
