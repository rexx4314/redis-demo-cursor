# Java 설치 및 JAVA_HOME 확인 스크립트

Write-Host "=== Java 설치 및 환경 변수 확인 ===" -ForegroundColor Cyan
Write-Host ""

# 1. 프로젝트 요구사항
Write-Host "1. 프로젝트 요구사항:" -ForegroundColor Yellow
Write-Host "   - Java 17 필요 (build.gradle: sourceCompatibility = '17')" -ForegroundColor White
Write-Host ""

# 2. Java 명령어 확인
Write-Host "2. Java 명령어 확인:" -ForegroundColor Yellow
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if ($javaCmd) {
    try {
        $javaVersion = java -version 2>&1 | Select-Object -First 1
        Write-Host "   [OK] Java가 설치되어 있습니다" -ForegroundColor Green
        Write-Host "   $javaVersion" -ForegroundColor White
    } catch {
        Write-Host "   [FAIL] Java 실행 실패" -ForegroundColor Red
    }
} else {
    Write-Host "   [FAIL] Java가 설치되어 있지 않거나 PATH에 없습니다" -ForegroundColor Red
}
Write-Host ""

# 3. JAVA_HOME 확인
Write-Host "3. JAVA_HOME 환경 변수 확인:" -ForegroundColor Yellow
$javaHome = $env:JAVA_HOME
if ($javaHome) {
    Write-Host "   [OK] JAVA_HOME: $javaHome" -ForegroundColor Green
    if (Test-Path "$javaHome\bin\java.exe") {
        Write-Host "   [OK] java.exe 파일이 존재합니다" -ForegroundColor Green
    } else {
        Write-Host "   [FAIL] java.exe 파일을 찾을 수 없습니다" -ForegroundColor Red
    }
} else {
    Write-Host "   [FAIL] JAVA_HOME이 설정되어 있지 않습니다" -ForegroundColor Red
}
Write-Host ""

# 4. 일반적인 Java 설치 경로 확인
Write-Host "4. 일반적인 Java 설치 경로 확인:" -ForegroundColor Yellow
$possiblePaths = @(
    "C:\Program Files\Java",
    "C:\Program Files (x86)\Java",
    "C:\Program Files\Eclipse Adoptium",
    "C:\Program Files\Eclipse Foundation"
)

$foundJava = $false
foreach ($path in $possiblePaths) {
    if (Test-Path $path) {
        $javaDirs = Get-ChildItem $path -Directory -ErrorAction SilentlyContinue | Where-Object { 
            $_.Name -like "jdk*" -or $_.Name -like "java*" 
        }
        if ($javaDirs) {
            $foundJava = $true
            Write-Host "   [FOUND] Java 설치 발견:" -ForegroundColor Green
            foreach ($dir in $javaDirs) {
                $javaExe = Join-Path $dir.FullName "bin\java.exe"
                if (Test-Path $javaExe) {
                    Write-Host "     - $($dir.FullName)" -ForegroundColor White
                }
            }
        }
    }
}

if (-not $foundJava) {
    Write-Host "   [NOT FOUND] 일반적인 경로에서 Java를 찾을 수 없습니다" -ForegroundColor Red
}
Write-Host ""

# 5. PATH에서 Java 확인
Write-Host "5. PATH 환경 변수에서 Java 확인:" -ForegroundColor Yellow
$pathEntries = $env:PATH -split ';' | Where-Object { $_ -like "*java*" -or $_ -like "*jdk*" }
if ($pathEntries) {
    Write-Host "   [FOUND] Java 관련 PATH:" -ForegroundColor Green
    foreach ($entry in $pathEntries) {
        Write-Host "     - $entry" -ForegroundColor White
    }
} else {
    Write-Host "   [NOT FOUND] PATH에 Java 관련 경로가 없습니다" -ForegroundColor Red
}
Write-Host ""

# 6. 권장 사항
Write-Host "=== 권장 사항 ===" -ForegroundColor Cyan
if (-not $javaHome) {
    Write-Host ""
    Write-Host "JAVA_HOME을 설정하려면:" -ForegroundColor Yellow
    Write-Host "1. Java 17 설치:" -ForegroundColor White
    Write-Host "   - Adoptium: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Gray
    Write-Host "   - Oracle JDK: https://www.oracle.com/java/technologies/downloads/#java17" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2. 설치 후 관리자 권한 PowerShell에서 실행:" -ForegroundColor White
    Write-Host "   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'Machine')" -ForegroundColor Gray
    Write-Host ""
    Write-Host "3. 새 PowerShell 창을 열어서 확인" -ForegroundColor White
} else {
    Write-Host "[OK] JAVA_HOME이 설정되어 있습니다." -ForegroundColor Green
    Write-Host "새 PowerShell 창을 열어서 테스트를 실행하세요: .\gradlew.bat test" -ForegroundColor White
}
