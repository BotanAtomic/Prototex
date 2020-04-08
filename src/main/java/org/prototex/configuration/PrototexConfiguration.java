package org.prototex.configuration;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.Charset;

@Builder()
@Data
public class PrototexConfiguration {

    @Builder.Default
    private final String host = "0.0.0.0";

    @Builder.Default
    private final int port = 4666;

    @Builder.Default
    private final int bufferSize = 2048;

    @Builder.Default
    private final boolean autoReconnect = true;

    @Builder.Default
    private final int reconnectionDelay = 10000;

    private final int bossCount;

    private final int workerCount;

    @Builder.Default
    private final Charset charset = Charset.defaultCharset();

    public static PrototexConfiguration empty() {
        return PrototexConfiguration.builder().build();
    }
}
