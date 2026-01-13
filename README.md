# Redis Demo 프로젝트

> 이 프로젝트는 Cursor AI가 생성한 예제 코드입니다. (redis-api-test.http 제외)

Spring Boot와 Redis를 활용한 데모 프로젝트입니다. 이 프로젝트는 Spring Data Redis를 사용하여 Redis와의 통합 방법을 보여주며, 로컬 Redis와 Testcontainers를 사용한 통합 테스트를 포함합니다.

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [요구사항](#요구사항)
- [프로젝트 구조](#프로젝트-구조)
- [설치 및 실행](#설치-및-실행)
- [테스트](#테스트)
- [주요 기능](#주요-기능)
- [설정](#설정)
- [문제 해결](#문제-해결)

## 🎯 프로젝트 개요

이 프로젝트는 Spring Boot 3.2.5와 Java 17을 기반으로 하며, Redis를 데이터 저장소로 활용하는 방법을 보여줍니다. 다음과 같은 기능을 포함합니다:

- Spring Data Redis를 통한 Redis 연동
- 로컬 Docker Redis를 사용한 통합 테스트
- Testcontainers를 사용한 격리된 통합 테스트
- Redis 기본 연산 (Set, Get, Expiration, Hash) 테스트

## 🛠 기술 스택

### 핵심 기술
- **Java**: 17
- **Spring Boot**: 3.2.5
- **Gradle**: 8.5 (Wrapper 포함)
- **Redis**: Spring Data Redis

### 주요 의존성
- `spring-boot-starter-web`: Spring Web 기능
- `spring-boot-starter-data-redis`: Spring Data Redis 통합
- `spring-boot-starter-test`: 테스트 프레임워크
- `testcontainers`: Docker 기반 통합 테스트

### 테스트 도구
- JUnit 5 (Jupiter)
- AssertJ: Fluent assertions
- Testcontainers: Docker 컨테이너 기반 테스트

## 📦 요구사항

### 필수 요구사항
- **Java 17** 이상
- **Gradle** (Wrapper 포함, 별도 설치 불필요)
- **Docker Desktop** (Testcontainers 테스트 실행 시 필요, 선택사항)

### 선택적 요구사항
- **로컬 Redis** (로컬 Redis 통합 테스트 실행 시 필요)
  - Docker를 사용하는 경우: `docker run -d -p 6379:6379 redis:7-alpine`

## 📁 프로젝트 구조

```
redis-demo-cursor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/redisdemo/
│   │   │       ├── RedisDemoApplication.java      # 메인 애플리케이션
│   │   │       └── config/
│   │   │           └── RedisConfig.java             # Redis 설정
│   │   └── resources/
│   │       └── application.properties               # 애플리케이션 설정
│   └── test/
│       ├── java/
│       │   └── com/example/redisdemo/
│       │       ├── RedisDemoApplicationTests.java           # 기본 컨텍스트 테스트
│       │       ├── LocalRedisIntegrationTest.java          # 로컬 Redis 통합 테스트
│       │       └── RedisTestcontainersIntegrationTest.java # Testcontainers 통합 테스트
│       └── resources/
│           └── testcontainers.properties                    # Testcontainers 설정
├── build.gradle                                            # Gradle 빌드 설정
├── settings.gradle                                          # Gradle 프로젝트 설정
├── gradlew.bat                                             # Gradle Wrapper (Windows)
└── README.md                                               # 프로젝트 문서
```

## 🚀 설치 및 실행

### 1. Java 17 설치 확인

Java 17이 설치되어 있는지 확인합니다:

```powershell
java -version
```

Java가 설치되어 있지 않은 경우, 다음 가이드를 참고하세요:
- [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md) - Java 설치 및 JAVA_HOME 설정 가이드
- [JAVA_SETUP_GUIDE.md](JAVA_SETUP_GUIDE.md) - 상세한 Java 설정 가이드

### 2. 프로젝트 빌드

```powershell
# Windows
.\gradlew.bat build

# 또는 clean 빌드
.\gradlew.bat clean build
```

### 3. 애플리케이션 실행

```powershell
.\gradlew.bat bootRun
```

애플리케이션이 실행되면 기본적으로 `http://localhost:18091`에서 접근할 수 있습니다.

### 4. JAR 파일 생성 및 실행

```powershell
# JAR 파일 생성
.\gradlew.bat bootJar

# 생성된 JAR 실행
java -jar build\libs\redis-demo-0.0.1-SNAPSHOT.jar
```

## 🧪 테스트

프로젝트에는 세 가지 유형의 테스트가 포함되어 있습니다:

### 1. 기본 컨텍스트 테스트
- **파일**: `RedisDemoApplicationTests.java`
- **목적**: Spring Boot 애플리케이션 컨텍스트가 정상적으로 로드되는지 확인
- **실행**: 항상 실행됨

### 2. 로컬 Redis 통합 테스트
- **파일**: `LocalRedisIntegrationTest.java`
- **목적**: 로컬 Docker Redis (`localhost:6379`)와의 통합 테스트
- **요구사항**: 로컬에서 Redis가 실행 중이어야 함
- **동작**: Redis가 없으면 자동으로 스킵됨

로컬 Redis 실행 방법:
```powershell
docker run -d -p 6379:6379 --name redis-local redis:7-alpine
```

### 3. Testcontainers 통합 테스트
- **파일**: `RedisTestcontainersIntegrationTest.java`
- **목적**: Testcontainers를 사용한 격리된 Redis 컨테이너 테스트
- **요구사항**: Docker Desktop이 실행 중이어야 함
- **동작**: Docker를 사용할 수 없으면 자동으로 스킵됨

### 테스트 실행

```powershell
# 모든 테스트 실행
.\gradlew.bat test

# 특정 테스트만 실행
.\gradlew.bat test --tests "RedisDemoApplicationTests"

# 테스트 리포트 확인
start build\reports\tests\test\index.html
```

### 테스트 결과

테스트 실행 후 다음 위치에서 상세한 리포트를 확인할 수 있습니다:
- HTML 리포트: `build/reports/tests/test/index.html`
- XML 결과: `build/test-results/test/`

## ✨ 주요 기능

### Redis 설정 (`RedisConfig.java`)

프로젝트는 `RedisTemplate<String, String>`을 사용하여 Redis와 상호작용합니다:

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        // String 직렬화를 사용하는 RedisTemplate 설정
    }
}
```

### 테스트된 Redis 연산

1. **기본 연산**
   - `SET`: 키-값 저장
   - `GET`: 키로 값 조회
   - `DELETE`: 키 삭제

2. **만료 시간 설정**
   - TTL(Time To Live)을 사용한 자동 만료

3. **Hash 연산**
   - Hash 구조 데이터 저장 및 조회

## ⚙️ 설정

### 애플리케이션 설정 (`application.properties`)

현재 기본 설정 파일은 비어있습니다. Redis 연결 설정을 추가하려면:

```properties
# Redis 연결 설정
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.database=0

# 연결 풀 설정 (선택사항)
spring.data.redis.lettuce.pool.max-active=8
spring.data.redis.lettuce.pool.max-idle=8
spring.data.redis.lettuce.pool.min-idle=0
```

### Testcontainers 설정 (`testcontainers.properties`)

Testcontainers가 Docker Desktop과 통신하도록 설정되어 있습니다:

```properties
# Docker Desktop on Windows
docker.client.strategy=org.testcontainers.dockerclient.NpipeSocketClientProviderStrategy
```

## 🔧 문제 해결

### Java 관련 문제

#### "JAVA_HOME is not set" 오류

Java가 설치되어 있지만 JAVA_HOME이 설정되지 않은 경우:

1. Java 설치 경로 확인
2. 시스템 환경 변수에 `JAVA_HOME` 설정
3. `Path`에 `%JAVA_HOME%\bin` 추가
4. 새 PowerShell 창 열기

자세한 내용은 [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)를 참고하세요.

### Docker 관련 문제

#### Testcontainers가 Docker를 찾지 못함

**증상**: `Could not find a valid Docker environment` 오류

**해결 방법**:
1. Docker Desktop이 실행 중인지 확인
2. Docker Desktop 재시작
3. Docker 명령어가 작동하는지 확인:
   ```powershell
   docker ps
   ```

**참고**: Docker가 없어도 테스트는 통과합니다. Testcontainers 테스트만 스킵됩니다.

### Gradle 관련 문제

#### 의존성 다운로드 실패

```powershell
# Gradle 캐시 정리
.\gradlew.bat clean --refresh-dependencies

# 또는 완전히 정리
Remove-Item -Recurse -Force .gradle
.\gradlew.bat build
```

#### Daemon 문제

```powershell
# Gradle Daemon 중지
.\gradlew.bat --stop
```

### Redis 연결 문제

#### 로컬 Redis 연결 실패

1. Redis가 실행 중인지 확인:
   ```powershell
   docker ps | findstr redis
   ```

2. 포트가 올바른지 확인 (기본값: 6379)

3. 방화벽 설정 확인

## 📚 추가 리소스

### 공식 문서
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Data Redis 문서](https://spring.io/projects/spring-data-redis)
- [Testcontainers 문서](https://www.testcontainers.org/)
- [Redis 공식 문서](https://redis.io/docs/)

### 유용한 가이드
- [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md) - Java 설치 및 환경 설정
- [JAVA_SETUP_GUIDE.md](JAVA_SETUP_GUIDE.md) - 상세한 Java 설정 가이드

## 📝 라이선스

이 프로젝트는 데모 목적으로 작성되었습니다.

## 🤝 기여

이 프로젝트는 학습 및 데모 목적으로 작성되었습니다. 개선 사항이나 버그 리포트는 이슈로 등록해 주세요.

## 📞 문의

프로젝트 관련 문의사항이 있으시면 이슈를 생성해 주세요.

---

**마지막 업데이트**: 2026년 1월
