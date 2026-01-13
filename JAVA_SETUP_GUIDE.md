# Java 17 설치 및 JAVA_HOME 설정 가이드 (Windows)

## 프로젝트 요구사항

이 프로젝트는 **Java 17**이 필요합니다.
- `build.gradle`에서 `sourceCompatibility = '17'`로 설정되어 있습니다.

---

## 1단계: Java 설치 여부 확인

### 현재 상태 확인 명령어

```powershell
# Java 명령어 확인
java -version

# JAVA_HOME 환경 변수 확인
$env:JAVA_HOME

# 일반적인 Java 설치 경로 확인
Get-ChildItem "C:\Program Files\Java" -ErrorAction SilentlyContinue
Get-ChildItem "C:\Program Files (x86)\Java" -ErrorAction SilentlyContinue

# PATH에서 java 찾기
where.exe java
```

**현재 상태**: Java가 설치되어 있지 않습니다.

---

## 2단계: Java 17 설치

### 방법 1: Oracle JDK 17 설치 (권장)

1. **다운로드 페이지 접속**
   - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - 또는 https://www.oracle.com/java/technologies/downloads/#java17

2. **Windows x64 Installer 다운로드**
   - `jdk-17_windows-x64_bin.exe` (또는 최신 버전)

3. **설치 실행**
   - 다운로드한 `.exe` 파일을 실행
   - 기본 설치 경로: `C:\Program Files\Java\jdk-17` (또는 버전별 경로)

### 방법 2: OpenJDK 17 설치 (무료, 권장)

1. **Adoptium (Eclipse Temurin) 다운로드**
   - https://adoptium.net/temurin/releases/?version=17
   - Windows x64, JDK 선택
   - `.msi` 설치 파일 다운로드

2. **설치 실행**
   - 다운로드한 `.msi` 파일을 실행
   - 설치 중 "Set JAVA_HOME variable" 옵션을 체크하면 자동으로 설정됩니다
   - 기본 설치 경로: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot`

### 방법 3: Chocolatey 사용 (패키지 매니저)

```powershell
# Chocolatey가 설치되어 있는 경우
choco install openjdk17
```

---

## 3단계: JAVA_HOME 환경 변수 설정

### 방법 A: PowerShell에서 임시 설정 (현재 세션만 유효)

```powershell
# Java 설치 경로 확인 (예시)
$javaPath = "C:\Program Files\Java\jdk-17"
# 또는
$javaPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.10+9-hotspot"

# JAVA_HOME 설정
$env:JAVA_HOME = $javaPath

# PATH에 추가
$env:PATH = "$javaPath\bin;$env:PATH"

# 확인
$env:JAVA_HOME
java -version
```

### 방법 B: 시스템 환경 변수 영구 설정 (권장)

#### GUI 방법:

1. **시스템 속성 열기**
   - `Win + R` → `sysdm.cpl` 입력 → Enter
   - 또는: 제어판 → 시스템 → 고급 시스템 설정

2. **환경 변수 버튼 클릭**

3. **시스템 변수 섹션에서 새로 만들기**
   - 변수 이름: `JAVA_HOME`
   - 변수 값: Java 설치 경로 (예: `C:\Program Files\Java\jdk-17`)
   - 확인 클릭

4. **Path 변수 편집**
   - 시스템 변수에서 `Path` 선택 → 편집
   - 새로 만들기 → `%JAVA_HOME%\bin` 추가
   - 확인 클릭

5. **모든 창 닫기**

#### PowerShell 관리자 권한으로 설정:

```powershell
# 관리자 권한 PowerShell에서 실행

# Java 설치 경로 (실제 경로로 변경 필요)
$javaPath = "C:\Program Files\Java\jdk-17"

# JAVA_HOME 시스템 환경 변수 설정
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaPath, [System.EnvironmentVariableTarget]::Machine)

# PATH에 추가
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$newPath = "$javaPath\bin;$currentPath"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::Machine)

# 새 PowerShell 창에서 확인 필요
```

---

## 4단계: 설치 확인

### 새 PowerShell 창 열기 (환경 변수 새로고침)

```powershell
# Java 버전 확인
java -version

# JAVA_HOME 확인
$env:JAVA_HOME

# javac 컴파일러 확인
javac -version

# Gradle 테스트 실행
.\gradlew.bat test
```

**예상 출력:**
```
openjdk version "17.0.x" 2024-xx-xx
OpenJDK Runtime Environment (build 17.0.x+x)
OpenJDK 64-Bit Server VM (build 17.0.x+x, mixed mode, sharing)
```

---

## 5단계: 문제 해결

### Java가 설치되었지만 인식되지 않는 경우

1. **PowerShell 재시작** (환경 변수 새로고침)

2. **설치 경로 확인**
   ```powershell
   # 일반적인 설치 경로들 확인
   Get-ChildItem "C:\Program Files\Java" -Recurse -Filter "java.exe" | Select-Object FullName
   Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Recurse -Filter "java.exe" | Select-Object FullName
   ```

3. **JAVA_HOME 경로 확인**
   - `bin` 폴더가 있는 상위 디렉토리여야 합니다
   - 예: `C:\Program Files\Java\jdk-17` (O)
   - 예: `C:\Program Files\Java\jdk-17\bin` (X)

4. **PATH 확인**
   ```powershell
   $env:PATH -split ';' | Select-String -Pattern "java"
   ```

### 여러 Java 버전이 설치된 경우

```powershell
# 모든 Java 버전 확인
Get-ChildItem "C:\Program Files\Java" | Select-Object Name

# 특정 버전으로 JAVA_HOME 설정
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

---

## 빠른 설치 스크립트 (참고용)

다음 스크립트는 Java가 설치되어 있다고 가정하고 JAVA_HOME만 설정합니다:

```powershell
# 관리자 권한 PowerShell에서 실행
$javaInstallPath = "C:\Program Files\Java\jdk-17"  # 실제 경로로 변경

if (Test-Path $javaInstallPath) {
    [System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaInstallPath, [System.EnvironmentVariableTarget]::Machine)
    Write-Host "JAVA_HOME이 설정되었습니다: $javaInstallPath" -ForegroundColor Green
    Write-Host "새 PowerShell 창을 열어서 확인하세요." -ForegroundColor Yellow
} else {
    Write-Host "Java가 해당 경로에 설치되어 있지 않습니다." -ForegroundColor Red
    Write-Host "설치 경로를 확인하세요." -ForegroundColor Yellow
}
```

---

## 요약

1. ✅ **Java 17 설치** (Adoptium 또는 Oracle JDK)
2. ✅ **JAVA_HOME 환경 변수 설정** (시스템 변수)
3. ✅ **PATH에 %JAVA_HOME%\bin 추가**
4. ✅ **새 PowerShell 창에서 확인**
5. ✅ **`.\gradlew.bat test` 실행**

설치가 완료되면 프로젝트 루트에서 다음 명령어로 테스트를 실행할 수 있습니다:

```powershell
.\gradlew.bat test
```
