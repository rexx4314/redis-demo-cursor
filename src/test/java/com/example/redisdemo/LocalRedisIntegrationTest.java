package com.example.redisdemo;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379"
})
class LocalRedisIntegrationTest {

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        // 로컬 Redis 연결 확인
        Assumptions.assumeTrue(redisTemplate != null, 
            "RedisTemplate is not available. Skipping local Redis tests.");
        
        boolean redisAvailable = checkLocalRedisAvailable();
        Assumptions.assumeTrue(redisAvailable, 
            "Local Redis is not available on localhost:6379. Skipping local Redis tests.");
    }

    private boolean checkLocalRedisAvailable() {
        try {
            // Redis 연결 테스트 - 간단한 ping 테스트
            String testKey = "test:local:ping";
            redisTemplate.opsForValue().set(testKey, "ping");
            redisTemplate.delete(testKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void testRedisConnection() {
        // Redis 연결 테스트
        assertThat(redisTemplate).isNotNull();
        
        String key = "test:local:connection";
        String value = "Hello Local Redis";
        
        redisTemplate.opsForValue().set(key, value);
        String retrieved = redisTemplate.opsForValue().get(key);
        
        assertThat(retrieved).isEqualTo(value);
        
        // 정리
        redisTemplate.delete(key);
    }

    @Test
    void testRedisSetAndGet() {
        String key = "test:local:setget";
        String value = "test-value-123";
        
        redisTemplate.opsForValue().set(key, value);
        String result = redisTemplate.opsForValue().get(key);
        
        assertThat(result).isEqualTo(value);
        
        redisTemplate.delete(key);
    }

    @Test
    void testRedisExpiration() throws InterruptedException {
        String key = "test:local:expire";
        String value = "expiring-value";
        
        Duration expiration = Objects.requireNonNull(Duration.ofSeconds(1));
        redisTemplate.opsForValue().set(key, value, expiration);
        
        String result = redisTemplate.opsForValue().get(key);
        assertThat(result).isEqualTo(value);
        
        Thread.sleep(1100);
        
        String expired = redisTemplate.opsForValue().get(key);
        assertThat(expired).isNull();
    }
}
