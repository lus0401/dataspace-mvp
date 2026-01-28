
# 1. .env 파일 경로
$envFile = ".\infra\.env"

if (-Not (Test-Path $envFile)) {
    Write-Error ".env file not found at $envFile"
    exit 1
}

# 2. .env 파일을 읽어서 환경변수로 등록
Get-Content $envFile | ForEach-Object {
    if ($_ -match "^\s*#") { return }      # 주석 무시
    if ($_ -match "^\s*$") { return }      # 빈 줄 무시

    $pair = $_ -split "=", 2
    if ($pair.Length -eq 2) {
        $name = $pair[0].Trim()
        $value = $pair[1].Trim()
        [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
    }
}

Write-Host "Environment variables loaded from .env"

# 3. Gradle 실행

.\gradlew :service:daps-service:bootRun
.\gradlew :service:broker-service:bootRun
.\gradlew :service:provider-service:bootRun
.\gradlew :service:clearinghouse-service:bootRun
.\gradlew :service:consumer-service:bootRun