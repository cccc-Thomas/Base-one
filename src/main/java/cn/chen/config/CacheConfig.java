package cn.chen.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userCache", "productCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)          // 初始大小
                .maximumSize(10000)            // 最大条目数
                .expireAfterWrite(60, TimeUnit.SECONDS) // 写入后过期时间
                .recordStats());               // 开启统计，方便监控
        return cacheManager;
    }
}