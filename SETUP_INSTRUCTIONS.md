# Java 17 설치 및 JAVA_HOME 설정 가이드 (Windows)

## 현재 상태 확인 결과

✅ **프로젝트 요구사항**: Java 17 필요  
❌ **Java 설치**: 설치되어 있지 않음  
❌ **JAVA_HOME**: 설정되어 있지 않음  
❌ **일반적인 설치 경로**: Java를 찾을 수 없음

---

## 단계별 설치 및 설정 가이드

### 1단계: Java 17 설치

#### 방법 A: Eclipse Adoptium (Temurin) - 권장 (무료, 오픈소스)

1. **다운로드 페이지 접속**
   ```
   https://adoptium.net/temurin/releases/?version=17
   ```

2. **다운로드 선택**
   - Operating System: **Windows**
   - Architecture: **x64**
   - Package Type: **JDK**
   - Version: **17** (LTS)
   - 다운로드 형식: **.msi** (설치 파일)

3. **설치 실행**
   - 다운로드한 `.msi` 파일을 실행
   - 설치 중 **"Set JAVA_HOME variable"** 옵션을 체크 (자동 설정)
   - 기본 설치 경로: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot`

#### 방법 B: Oracle JDK 17

1. **다운로드 페이지 접속**
   ```
   https://www.oracle.com/java/technologies/downloads/#java17
   ```

2. **Windows x64 Installer 다운로드**
   - `jdk-17_windows-x64_bin.exe` 파일 다운로드

3. **설치 실행**
   - 다운로드한 `.exe` 파일을 실행
   - 기본 설치 경로: `C:\Program Files\Java\jdk-17`

---

### 2단계: JAVA_HOME 환경 변수 설정

#### 방법 1: GUI를 통한 설정 (권장)

1. **시스템 속성 열기**
   - `Win + R` 키를 누르고 `sysdm.cpl` 입력 후 Enter
   - 또는: 제어판 → 시스템 → 고급 시스템 설정

2. **환경 변수 버튼 클릭**

3. **시스템 변수 섹션에서 새로 만들기**
   - 변수 이름: `JAVA_HOME`
   - 변수 값: Java 설치 경로 (예시)
     ```
     C:\Program Files\Eclipse Adoptium\jdk-17.0.10+9-hotspot
     ```
     또는
     ```
     C:\Program Files\Java\jdk-17
     ```
   - **확인** 클릭

4. **Path 변수 편집**
   - 시스템 변수에서 `Path` 선택 → **편집** 클릭
   - **새로 만들기** 클릭
   - 다음 값 추가: `%JAVA_HOME%\bin`
   - **확인** 클릭

5. **모든 창 닫기**

#### 방법 2: PowerShell 관리자 권한으로 설정

**중요**: 관리자 권한으로 PowerShell을 실행해야 합니다.

```powershell
# 1. Java 설치 경로 확인 (실제 경로로 변경 필요)
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.10+9-hotspot"
# 또는
$javaPath = "C:\Program Files\Java\jdk-17"

# 2. JAVA_HOME 시스템 환경 변수 설정
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaPath, [System.EnvironmentVariableTarget]::Machine)

# 3. PATH에 추가
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$newPath = "$javaPath\bin;$currentPath"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::Machine)

Write-Host "JAVA_HOME이 설정되었습니다: $javaPath" -ForegroundColor Green
Write-Host "새 PowerShell 창을 열어서 확인하세요." -ForegroundColor Yellow
```

**참고**: 실제 Java 설치 경로를 확인하려면:
```powershell
Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory | Select-Object Name
Get-ChildItem "C:\Program Files\Java" -Directory | Select-Object Name
```

---

### 3단계: 설치 확인

**중요**: 환경 변수 변경 후 **새 PowerShell 창**을 열어야 합니다.

#### 확인 명령어

```powershell
# 1. Java 버전 확인
java -version

# 예상 출력:
# openjdk version "17.0.x" 2024-xx-xx
# OpenJDK Runtime Environment (build 17.0.x+x)
# OpenJDK 64-Bit Server VM (build 17.0.x+x, mixed mode, sharing)

# 2. JAVA_HOME 확인
$env:JAVA_HOME

# 3. javac 컴파일러 확인
javac -version

# 4. Gradle 테스트 실행
.\gradlew.bat test
```

---

### 4단계: 문제 해결

#### Java가 설치되었지만 인식되지 않는 경우

1. **PowerShell 재시작** (환경 변수 새로고침)

2. **설치 경로 확인**
   ```powershell
   # 일반적인 설치 경로 확인
   Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Recurse -Filter "java.exe" | Select-Object FullName
   Get-ChildItem "C:\Program Files\Java" -Recurse -Filter "java.exe" | Select-Object FullName
   ```

3. **JAVA_HOME 경로 확인**
   - `bin` 폴더가 있는 **상위 디렉토리**여야 합니다
   - ✅ 올바른 예: `C:\Program Files\Java\jdk-17`
   - ❌ 잘못된 예: `C:\Program Files\Java\jdk-17\bin`

4. **PATH 확인**
   ```powershell
   $env:PATH -split ';' | Select-String -Pattern "java"
   ```

#### 여러 Java 버전이 설치된 경우

```powershell
# 모든 Java 버전 확인
Get-ChildItem "C:\Program Files\Java" | Select-Object Name
Get-ChildItem "C:\Program Files\Eclipse Adoptium" | Select-Object Name

# 특정 버전으로 JAVA_HOME 설정
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

---

## 빠른 참조: 설치 확인 스크립트

프로젝트 루트에 있는 `check-java.ps1` 스크립트를 실행하여 현재 상태를 확인할 수 있습니다:

```powershell
powershell -ExecutionPolicy Bypass -File .\check-java.ps1
```

---

## 요약 체크리스트

- [ ] Java 17 설치 (Adoptium 또는 Oracle JDK)
- [ ] JAVA_HOME 환경 변수 설정 (시스템 변수)
- [ ] PATH에 `%JAVA_HOME%\bin` 추가
- [ ] 새 PowerShell 창 열기
- [ ] `java -version` 명령어로 확인
- [ ] `.\gradlew.bat test` 실행

---

## 추가 리소스

- **Eclipse Adoptium**: https://adoptium.net/
- **Oracle JDK**: https://www.oracle.com/java/
- **Spring Boot 공식 문서**: https://spring.io/projects/spring-boot
