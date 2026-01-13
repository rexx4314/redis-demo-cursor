# Redis API 간단 테스트 스크립트

$baseUrl = "http://localhost:18091/api/redis"

Write-Host "=== Redis API 테스트 ===" -ForegroundColor Cyan
Write-Host ""

# Health Check
Write-Host "1. Health Check" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/health" -Method Get
$response | ConvertTo-Json
Write-Host ""

# SET
Write-Host "2. 키-값 저장" -ForegroundColor Yellow
$setBody = @{
    key = "test:key1"
    value = "Hello Redis"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "$baseUrl/set" -Method Post -Body $setBody -ContentType "application/json"
$response | ConvertTo-Json
Write-Host ""

# GET
Write-Host "3. 값 조회" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/get/test:key1" -Method Get
$response | ConvertTo-Json
Write-Host ""

# EXISTS
Write-Host "4. 키 존재 여부" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/exists/test:key1" -Method Get
$response | ConvertTo-Json
Write-Host ""

# KEYS
Write-Host "5. 키 목록 조회" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/keys?pattern=test:*" -Method Get
$response | ConvertTo-Json
Write-Host ""

# HASH SET
Write-Host "6. Hash 필드 설정" -ForegroundColor Yellow
$hashBody = @{
    hashKey = "user:1"
    field = "name"
    value = "John Doe"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "$baseUrl/hash/set" -Method Post -Body $hashBody -ContentType "application/json"
$response | ConvertTo-Json
Write-Host ""

# HASH GET
Write-Host "7. Hash 필드 조회" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/hash/get/user:1/name" -Method Get
$response | ConvertTo-Json
Write-Host ""

# DELETE
Write-Host "8. 키 삭제" -ForegroundColor Yellow
$response = Invoke-RestMethod -Uri "$baseUrl/delete/test:key1" -Method Delete
$response | ConvertTo-Json
Write-Host ""

Write-Host "=== 테스트 완료 ===" -ForegroundColor Green
