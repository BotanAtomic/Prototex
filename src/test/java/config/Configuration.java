package config;

import org.prototex.configuration.PrototexConfiguration;

/**
 * Created by Botan on 4/6/2020. 11:27 PM
 **/
public class Configuration {

    public static final PrototexConfiguration configuration = PrototexConfiguration.builder()
            .bufferSize(4096 * 5)
            .port(6999)
            .bossCount(10)
            .workerCount(20)
            .autoReconnect(true)
            .reconnectionDelay(2000)
            .build();

}
