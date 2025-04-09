package com.store;

import com.store.configuration.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@SpringBootTest
@EnableCaching
@Import(TestSecurityConfig.class)
class OnlineStoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
