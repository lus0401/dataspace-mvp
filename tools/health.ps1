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
