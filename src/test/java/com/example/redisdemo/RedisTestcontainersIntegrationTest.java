package com.example.redisdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@EnabledIf("com.example.redisdemo.RedisTestcontainersIntegrationTest#isDockerAvailable")
class RedisTestcontainersIntegrationTest {

    @Container
    @SuppressWarnings("resource")
    static final GenericContainer<?> redis = new GenericContainer<>(
        DockerImageName.parse("redis:7-alpine")
    )
        .withExposedPorts(6379);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @SuppressWarnings("unused")
    static boolean isDockerAvailable() {
        try {
            DockerClientFactory.instance().client();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        assertThat(redisTemplate).isNotNull();
    }

    @Test
    void testRedisConnection() {
        // Redis 연결 테스트
        String key = "test:containers:connection";
        String value = "Hello Testcontainers Redis";
        
        redisTemplate.opsForValue().set(key, value);
        String retrieved = redisTemplate.opsForValue().get(key);
        
        assertThat(retrieved).isEqualTo(value);
        
        // 정리
        redisTemplate.delete(key);
    }

    @Test
    void testRedisSetAndGet() {
        String key = "test:containers:setget";
        String value = "test-value-456";
        
        redisTemplate.opsForValue().set(key, value);
        String result = redisTemplate.opsForValue().get(key);
        
        assertThat(result).isEqualTo(value);
        
        redisTemplate.delete(key);
    }

    @Test
    void testRedisExpiration() throws InterruptedException {
        String key = "test:containers:expire";
        String value = "expiring-value";
        
        Duration expiration = Objects.requireNonNull(Duration.ofSeconds(1));
        redisTemplate.opsForValue().set(key, value, expiration);
        
        String result = redisTemplate.opsForValue().get(key);
        assertThat(result).isEqualTo(value);
        
        Thread.sleep(1100);
        
        String expired = redisTemplate.opsForValue().get(key);
        assertThat(expired).isNull();
    }

    @Test
    void testRedisHashOperations() {
        String hashKey = "test:containers:hash";
        String field = "field1";
        String value = "hash-value";
        
        redisTemplate.opsForHash().put(hashKey, field, value);
        Object retrieved = redisTemplate.opsForHash().get(hashKey, field);
        
        assertThat(retrieved).isEqualTo(value);
        
        redisTemplate.delete(hashKey);
    }
}
