Write-Host "=== Dataspace MVP End-to-End Test ===" -ForegroundColor Cyan

function Check-Health($name, $url) {
    try {
        $res = Invoke-RestMethod -Uri $url -TimeoutSec 3
        Write-Host "[OK] $name is UP" -ForegroundColor Green
        return $true
    } catch {
        Write-Host "[FAIL] $name is DOWN -> $url" -ForegroundColor Red
        return $false
    }
}

# 1. Health Check
Write-Host "`n[1] Health Check" -ForegroundColor Yellow

$healthOk = $true
$healthOk = $healthOk -and (Check-Health "DAPS" "http://localhost:8081/actuator/health")
$healthOk = $healthOk -and (Check-Health "Broker" "http://localhost:8082/actuator/health")
$healthOk = $healthOk -and (Check-Health "Provider" "http://localhost:8083/actuator/health")
$healthOk = $healthOk -and (Check-Health "Consumer" "http://localhost:8084/actuator/health")

# Clearinghouse는 actuator 없을 수도 있어서 events로 체크
$healthOk = $healthOk -and (Check-Health "Clearinghouse" "http://localhost:8085/events")

if (-not $healthOk) {
    Write-Host "`n[FAIL] Some services are not running. Stop test." -ForegroundColor Red
    exit 1
}

# 2. Register Offer
Write-Host "`n[2] Register Offer to Broker" -ForegroundColor Yellow

$offerBody = @{
    title       = "Provider Data Offer"
    description = "Fetch /data from provider"
    providerUrl = "http://localhost:8083"
    ownerId     = "provider-1"
    dataType    = "application/json"
} | ConvertTo-Json

try {
    $offer = Invoke-RestMethod -Method Post `
        -Uri "http://localhost:8082/offers" `
        -ContentType "application/json" `
        -Body $offerBody

    Write-Host "[OK] Offer registered (id=$($offer.id))" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Offer registration failed" -ForegroundColor Red
    throw
}

# 3. Fetch via Consumer
Write-Host "`n[3] Consumer Fetch" -ForegroundColor Yellow

try {
    $fetchResult = Invoke-RestMethod -Uri "http://localhost:8084/fetch"
    Write-Host "[OK] Fetch succeeded" -ForegroundColor Green
    $fetchResult | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[FAIL] Fetch failed" -ForegroundColor Red
    throw
}

# 4. Check Clearinghouse Events
Write-Host "`n[4] Clearinghouse Events" -ForegroundColor Yellow

try {
    $events = Invoke-RestMethod -Uri "http://localhost:8085/events"
    Write-Host "[OK] Clearinghouse events fetched" -ForegroundColor Green
    $events | ConvertTo-Json -Depth 5
} catch {
    Write-Host "[FAIL] Clearinghouse event fetch failed" -ForegroundColor Red
    throw
}

Write-Host "`n ALL TESTS PASSED " -ForegroundColor Cyan
