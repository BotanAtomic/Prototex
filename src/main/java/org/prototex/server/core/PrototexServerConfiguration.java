package org.prototex.server.core;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrototexServerConfiguration {

    private final String host;

    private final int port;

    @Builder.Default
    private int bossCount = 1;

    @Builder.Default
    private int workerCount = 1;

    private final int bufferSize;

    public static PrototexServerConfiguration empty() {
        return builder().build();
    }

}
