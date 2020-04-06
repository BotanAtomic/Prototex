package org.prototex.configuration;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.Charset;

@Builder()
@Data
public class PrototexConfiguration {

    @Builder.Default
    private final String host = "0.0.0.0";

    private final int port;

    @Builder.Default
    private final int bufferSize = 2048;

    private final int bossCount;

    private final int workerCount;

    @Builder.Default
    private final Charset charset = Charset.defaultCharset();

    public static PrototexConfiguration empty() {
        return PrototexConfiguration.builder().build();
    }
}
