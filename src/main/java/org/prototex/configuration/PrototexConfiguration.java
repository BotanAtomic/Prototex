package org.prototex.configuration;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.Charset;

@Builder
@Data
public class PrototexConfiguration {

    private final String host;

    private final int port;

    @Builder.Default
    private final int bufferSize = 2048;

    @Builder.Default
    private final int bossCount = 1;

    @Builder.Default
    private final int workerCount = 1;

    @Builder.Default
    private final Charset charset = Charset.defaultCharset();

    public static PrototexConfiguration empty() {
        return PrototexConfiguration.builder().build();
    }
}
