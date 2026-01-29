# Dataspace MVP Service Runner
# Multi-module structure compatible script

# Set UTF-8 encoding for console output
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
chcp 65001 | Out-Null

# Change to script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$rootDir = Split-Path -Parent $scriptDir
Set-Location $rootDir

# 1. Load .env file (optional)
$envFile = Join-Path $rootDir "infra\.env"

if (Test-Path $envFile) {
    Write-Host "[INFO] Loading environment variables from .env..." -ForegroundColor Yellow
    Get-Content $envFile -Encoding UTF8 | ForEach-Object {
        if ($_ -match "^\s*#") { return }
        if ($_ -match "^\s*$") { return }

        $pair = $_ -split "=", 2
        if ($pair.Length -eq 2) {
            $name = $pair[0].Trim()
            $value = $pair[1].Trim()
            [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
        }
    }
    Write-Host "[INFO] Environment variables loaded" -ForegroundColor Green
} else {
    Write-Host "[WARN] .env file not found: $envFile (optional)" -ForegroundColor Yellow
}

Write-Host "`n=== Dataspace MVP Service Runner ===" -ForegroundColor Green
Write-Host "Starting services in sequence...`n" -ForegroundColor Cyan

# 2. Check if gradlew exists
$gradlew = Join-Path $rootDir "gradlew.bat"
if (-not (Test-Path $gradlew)) {
    Write-Host "[ERROR] gradlew.bat not found in $rootDir" -ForegroundColor Red
    exit 1
}

# 3. Run services (sequential execution)
Write-Host "[1/5] Starting DAPS Service (port 8081)..." -ForegroundColor Cyan
& $gradlew :service:daps:service-daps-api:bootRun
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] DAPS service failed to start" -ForegroundColor Red
    exit 1
}

Write-Host "`n[2/5] Starting Broker Service (port 8082)..." -ForegroundColor Cyan
& $gradlew :service:broker:service-broker-api:bootRun
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Broker service failed to start" -ForegroundColor Red
    exit 1
}

Write-Host "`n[3/5] Starting Provider Service (port 8083)..." -ForegroundColor Cyan
& $gradlew :service:provider:service-provider-api:bootRun
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Provider service failed to start" -ForegroundColor Red
    exit 1
}

Write-Host "`n[4/5] Starting Clearinghouse Service (port 8085)..." -ForegroundColor Cyan
& $gradlew :service:clearinghouse:service-clearinghouse-api:bootRun
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Clearinghouse service failed to start" -ForegroundColor Red
    exit 1
}

Write-Host "`n[5/5] Starting Consumer Service (port 8084)..." -ForegroundColor Cyan
& $gradlew :service:consumer:service-consumer-api:bootRun
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Consumer service failed to start" -ForegroundColor Red
    exit 1
}
