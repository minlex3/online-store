package com.store.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class EmbeddedRedisConfiguration {

    @Bean(destroyMethod = "stop") // Останавливаем сервер при закрытии контекста
    @Primary
    public RedisServer redisServer() throws IOException {
        var redisServer = new RedisServer();
        redisServer.start(); // Запускаем прямо во время инициализации бина
        return redisServer;
    }
}
