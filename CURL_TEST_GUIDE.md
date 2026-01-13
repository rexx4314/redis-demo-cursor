# curl을 사용한 Redis API 테스트 가이드

이 문서는 Redis Demo 프로젝트의 REST API를 curl 명령어로 테스트하는 방법을 설명합니다.

## 📋 사전 준비

1. **애플리케이션 실행**
   ```powershell
   .\gradlew.bat bootRun
   ```
   또는
   ```powershell
   java -jar build\libs\redis-demo-0.0.1-SNAPSHOT.jar
   ```

2. **Redis 실행 확인**
   ```powershell
   docker ps | findstr redis
   ```
   Redis가 실행 중이어야 합니다.

## 🚀 API 엔드포인트 목록

**기본 URL**: `http://localhost:18091/api/redis`

### 1. Health Check
Redis 연결 상태를 확인합니다.

```powershell
curl -X GET http://localhost:18091/api/redis/health
```

**응답 예시:**
```json
{
  "status": "UP",
  "redis": "connected"
}
```

### 2. 키-값 저장 (SET)
Redis에 키-값 쌍을 저장합니다.

```powershell
curl -X POST http://localhost:18091/api/redis/set ^
  -H "Content-Type: application/json" ^
  -d "{\"key\":\"test:key1\",\"value\":\"Hello Redis\"}"
```

**TTL(만료 시간)과 함께 저장:**
```powershell
curl -X POST "http://localhost:18091/api/redis/set?ttl=10" ^
  -H "Content-Type: application/json" ^
  -d "{\"key\":\"test:temp\",\"value\":\"This will expire in 10 seconds\"}"
```

**응답 예시:**
```json
{
  "success": true,
  "key": "test:key1",
  "value": "Hello Redis"
}
```

### 3. 값 조회 (GET)
키로 값을 조회합니다.

```powershell
curl -X GET http://localhost:18091/api/redis/get/test:key1
```

**응답 예시:**
```json
{
  "success": true,
  "key": "test:key1",
  "value": "Hello Redis"
}
```

**키가 없는 경우:**
```json
{
  "success": false,
  "error": "Key not found"
}
```

### 4. 키 존재 여부 확인
키가 존재하는지 확인합니다.

```powershell
curl -X GET http://localhost:18091/api/redis/exists/test:key1
```

**응답 예시:**
```json
{
  "success": true,
  "key": "test:key1",
  "exists": true
}
```

### 5. 키 목록 조회
패턴에 맞는 키 목록을 조회합니다.

```powershell
curl -X GET "http://localhost:18091/api/redis/keys?pattern=test:*"
```

**응답 예시:**
```json
{
  "success": true,
  "pattern": "test:*",
  "count": 2,
  "keys": ["test:key1", "test:key2"]
}
```

### 6. 키 삭제 (DELETE)
키를 삭제합니다.

```powershell
curl -X DELETE http://localhost:18091/api/redis/delete/test:key1
```

**응답 예시:**
```json
{
  "success": true,
  "key": "test:key1",
  "message": "Key deleted successfully"
}
```

### 7. Hash 필드 설정
Hash 구조에 필드를 설정합니다.

```powershell
curl -X POST http://localhost:18091/api/redis/hash/set ^
  -H "Content-Type: application/json" ^
  -d "{\"hashKey\":\"user:1\",\"field\":\"name\",\"value\":\"John Doe\"}"
```

**응답 예시:**
```json
{
  "success": true,
  "hashKey": "user:1",
  "field": "name",
  "value": "John Doe"
}
```

### 8. Hash 필드 조회
Hash 구조에서 필드를 조회합니다.

```powershell
curl -X GET http://localhost:18091/api/redis/hash/get/user:1/name
```

**응답 예시:**
```json
{
  "success": true,
  "hashKey": "user:1",
  "field": "name",
  "value": "John Doe"
}
```

## 📝 전체 테스트 시나리오

다음은 전체 테스트 시나리오입니다:

```powershell
# 1. Health Check
curl -X GET http://localhost:18091/api/redis/health

# 2. 키-값 저장
curl -X POST http://localhost:18091/api/redis/set ^
  -H "Content-Type: application/json" ^
  -d "{\"key\":\"test:key1\",\"value\":\"Hello Redis\"}"

# 3. 값 조회
curl -X GET http://localhost:18091/api/redis/get/test:key1

# 4. TTL이 있는 키 저장 (10초 후 만료)
curl -X POST "http://localhost:18091/api/redis/set?ttl=10" ^
  -H "Content-Type: application/json" ^
  -d "{\"key\":\"test:temp\",\"value\":\"Temporary value\"}"

# 5. 키 존재 여부 확인
curl -X GET http://localhost:18091/api/redis/exists/test:key1

# 6. 모든 키 조회
curl -X GET "http://localhost:18091/api/redis/keys?pattern=test:*"

# 7. Hash 필드 설정
curl -X POST http://localhost:18091/api/redis/hash/set ^
  -H "Content-Type: application/json" ^
  -d "{\"hashKey\":\"user:1\",\"field\":\"name\",\"value\":\"John Doe\"}"

curl -X POST http://localhost:18091/api/redis/hash/set ^
  -H "Content-Type: application/json" ^
  -d "{\"hashKey\":\"user:1\",\"field\":\"email\",\"value\":\"john@example.com\"}"

# 8. Hash 필드 조회
curl -X GET http://localhost:18091/api/redis/hash/get/user:1/name
curl -X GET http://localhost:18091/api/redis/hash/get/user:1/email

# 9. 키 삭제
curl -X DELETE http://localhost:18091/api/redis/delete/test:key1

# 10. 삭제 확인
curl -X GET http://localhost:18091/api/redis/get/test:key1
```

## 🔧 PowerShell에서 JSON 포맷팅

PowerShell에서 응답을 보기 좋게 포맷팅하려면:

```powershell
curl -s -X GET http://localhost:18091/api/redis/get/test:key1 | ConvertFrom-Json | ConvertTo-Json -Depth 3
```

또는:

```powershell
$response = curl -s -X GET http://localhost:18091/api/redis/get/test:key1
$response | ConvertFrom-Json | Format-List
```

## ⚠️ 문제 해결

### 404 Not Found 오류

1. **애플리케이션이 실행 중인지 확인:**
   ```powershell
   netstat -ano | findstr :18091
   ```

2. **애플리케이션 재시작:**
   ```powershell
   .\gradlew.bat clean build
   .\gradlew.bat bootRun
   ```

### Redis 연결 오류

1. **Redis가 실행 중인지 확인:**
   ```powershell
   docker ps | findstr redis
   ```

2. **Redis 시작:**
   ```powershell
   docker run -d -p 6379:6379 --name redis-local redis:7-alpine
   ```

### JSON 파싱 오류

Windows PowerShell에서 JSON 문자열을 전달할 때 따옴표 이스케이프가 필요합니다:

```powershell
# 올바른 방법
curl -X POST http://localhost:18091/api/redis/set ^
  -H "Content-Type: application/json" ^
  -d "{\"key\":\"test\",\"value\":\"hello\"}"

# 또는 파일 사용
$body = Get-Content request.json -Raw
curl -X POST http://localhost:18091/api/redis/set ^
  -H "Content-Type: application/json" ^
  -d $body
```

## 📚 추가 리소스

- [curl 공식 문서](https://curl.se/docs/)
- [Spring Boot REST API 가이드](https://spring.io/guides/gs/rest-service/)
- [Redis 명령어 참조](https://redis.io/commands/)
