package com.example.redisdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 키-값 저장
     * POST /api/redis/set
     * Body: {"key": "test", "value": "hello"}
     */
    @PostMapping("/set")
    public ResponseEntity<Map<String, Object>> set(
            @RequestBody Map<String, String> request,
            @RequestParam(required = false) Long ttl) {
        String key = request.get("key");
        String value = request.get("value");

        if (key == null || value == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "key and value are required");
            return ResponseEntity.badRequest().body(error);
        }

        if (ttl != null && ttl > 0) {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
        } else {
            redisTemplate.opsForValue().set(key, value);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("key", key);
        response.put("value", value);
        if (ttl != null) {
            response.put("ttl", ttl);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 키로 값 조회
     * GET /api/redis/get/{key}
     */
    @GetMapping("/get/{key}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String key) {
        String value = redisTemplate.opsForValue().get(key);

        Map<String, Object> response = new HashMap<>();
        if (value != null) {
            response.put("success", true);
            response.put("key", key);
            response.put("value", value);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Key not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * 키 삭제
     * DELETE /api/redis/delete/{key}
     */
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String key) {
        Boolean deleted = redisTemplate.delete(key);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("key", key);
        if (deleted) {
            response.put("message", "Key deleted successfully");
        } else {
            response.put("message", "Key not found");
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 키 조회 (패턴 매칭)
     * GET /api/redis/keys?pattern=*
     */
    @GetMapping("/keys")
    public ResponseEntity<Map<String, Object>> keys(@RequestParam(defaultValue = "*") String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("pattern", pattern);
        response.put("count", keys != null ? keys.size() : 0);
        response.put("keys", keys != null ? keys : Set.of());
        return ResponseEntity.ok(response);
    }

    /**
     * 키 존재 여부 확인
     * GET /api/redis/exists/{key}
     */
    @GetMapping("/exists/{key}")
    public ResponseEntity<Map<String, Object>> exists(@PathVariable String key) {
        Boolean exists = redisTemplate.hasKey(key);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("key", key);
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    /**
     * Hash 필드 설정
     * POST /api/redis/hash/set
     * Body: {"hashKey": "user:1", "field": "name", "value": "John"}
     */
    @PostMapping("/hash/set")
    public ResponseEntity<Map<String, Object>> hashSet(@RequestBody Map<String, String> request) {
        String hashKey = request.get("hashKey");
        String field = request.get("field");
        String value = request.get("value");

        if (hashKey == null || field == null || value == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "hashKey, field, and value are required");
            return ResponseEntity.badRequest().body(error);
        }

        redisTemplate.opsForHash().put(hashKey, field, value);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("hashKey", hashKey);
        response.put("field", field);
        response.put("value", value);
        return ResponseEntity.ok(response);
    }

    /**
     * Hash 필드 조회
     * GET /api/redis/hash/get/{hashKey}/{field}
     */
    @GetMapping("/hash/get/{hashKey}/{field}")
    public ResponseEntity<Map<String, Object>> hashGet(
            @PathVariable String hashKey,
            @PathVariable String field) {
        Object value = redisTemplate.opsForHash().get(hashKey, field);

        Map<String, Object> response = new HashMap<>();
        if (value != null) {
            response.put("success", true);
            response.put("hashKey", hashKey);
            response.put("field", field);
            response.put("value", value);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Field not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Redis 연결 상태 확인
     * GET /api/redis/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("redis", "connected");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "DOWN");
            response.put("redis", "disconnected");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}
