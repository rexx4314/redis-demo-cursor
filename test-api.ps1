# Redis Demo API 테스트 스크립트

Write-Host "=== Redis Demo API 테스트 ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:18091/api/redis"

# 1. Health Check
Write-Host "1. Health Check" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/health" -H "Content-Type: application/json"
Write-Host ""

# 2. 키-값 저장
Write-Host "2. 키-값 저장 (SET)" -ForegroundColor Yellow
curl.exe -X POST "$baseUrl/set" `
    -H "Content-Type: application/json" `
    -d '{\"key\":\"test:key1\",\"value\":\"Hello Redis\"}'
Write-Host ""

# 3. 값 조회
Write-Host "3. 값 조회 (GET)" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/get/test:key1" -H "Content-Type: application/json"
Write-Host ""

# 4. TTL이 있는 키 저장
Write-Host "4. TTL이 있는 키 저장 (10초)" -ForegroundColor Yellow
curl.exe -X POST "$baseUrl/set?ttl=10" `
    -H "Content-Type: application/json" `
    -d '{\"key\":\"test:temp\",\"value\":\"This will expire\"}'
Write-Host ""

# 5. 키 존재 여부 확인
Write-Host "5. 키 존재 여부 확인" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/exists/test:key1" -H "Content-Type: application/json"
Write-Host ""

# 6. 모든 키 조회
Write-Host "6. 모든 키 조회" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/keys?pattern=test:*" -H "Content-Type: application/json"
Write-Host ""

# 7. Hash 필드 설정
Write-Host "7. Hash 필드 설정" -ForegroundColor Yellow
curl.exe -X POST "$baseUrl/hash/set" `
    -H "Content-Type: application/json" `
    -d '{\"hashKey\":\"user:1\",\"field\":\"name\",\"value\":\"John Doe\"}'
Write-Host ""

# 8. Hash 필드 조회
Write-Host "8. Hash 필드 조회" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/hash/get/user:1/name" -H "Content-Type: application/json"
Write-Host ""

# 9. 키 삭제
Write-Host "9. 키 삭제" -ForegroundColor Yellow
curl.exe -X DELETE "$baseUrl/delete/test:key1" -H "Content-Type: application/json"
Write-Host ""

# 10. 삭제 확인
Write-Host "10. 삭제 확인" -ForegroundColor Yellow
curl.exe -X GET "$baseUrl/get/test:key1" -H "Content-Type: application/json"
Write-Host ""

Write-Host "=== 테스트 완료 ===" -ForegroundColor Green
