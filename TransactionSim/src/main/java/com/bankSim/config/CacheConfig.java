package com.bankSim.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // By default, use simple in-memory ConcurrentHashMap cache.
        // It provides fast local caching for frequent queries like accounts and loans.
        return new ConcurrentMapCacheManager("accounts", "loans", "users");
    }
}
