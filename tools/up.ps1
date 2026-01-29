# Dataspace MVP Docker Compose Up Script
# Starts PostgreSQL container

# Set UTF-8 encoding for console output
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
chcp 65001 | Out-Null

# Get root directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$rootDir = Split-Path -Parent $scriptDir
$composeFile = Join-Path $rootDir "infra\docker-compose.yml"

Write-Host "=== Dataspace MVP Docker Compose Up ===" -ForegroundColor Green

# Check if Docker is available
Write-Host "`n[1/3] Checking Docker..." -ForegroundColor Yellow
try {
    $dockerVersion = docker --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  [INFO] $dockerVersion" -ForegroundColor Green
    } else {
        Write-Host "  [ERROR] Docker is not installed or not in PATH" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "  [ERROR] Docker command failed: $_" -ForegroundColor Red
    exit 1
}

# Check if Docker daemon is running
Write-Host "`n[2/3] Checking Docker daemon..." -ForegroundColor Yellow
try {
    $dockerInfo = docker info 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  [INFO] Docker daemon is running" -ForegroundColor Green
    } else {
        Write-Host "  [ERROR] Docker daemon is not running" -ForegroundColor Red
        Write-Host "`n  Please start Docker Desktop and wait for it to fully start." -ForegroundColor Yellow
        Write-Host "  You can start Docker Desktop from:" -ForegroundColor Yellow
        Write-Host "    - Start menu: Search for 'Docker Desktop'" -ForegroundColor Cyan
        Write-Host "    - Or run: Start-Process 'C:\Program Files\Docker\Docker\Docker Desktop.exe'" -ForegroundColor Cyan
        exit 1
    }
} catch {
    Write-Host "  [ERROR] Cannot connect to Docker daemon: $_" -ForegroundColor Red
    Write-Host "`n  Please start Docker Desktop and wait for it to fully start." -ForegroundColor Yellow
    exit 1
}

# Start containers
Write-Host "`n[3/3] Starting PostgreSQL container..." -ForegroundColor Yellow
Set-Location $rootDir
docker compose -f $composeFile up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=== Docker Compose Up Successful ===" -ForegroundColor Green
    Write-Host "`nPostgreSQL container is running:" -ForegroundColor Cyan
    Write-Host "  - Host: localhost" -ForegroundColor White
    Write-Host "  - Port: 5432" -ForegroundColor White
    Write-Host "  - Database: dataspace" -ForegroundColor White
    Write-Host "  - User: dataspace" -ForegroundColor White
    Write-Host "  - Password: dataspace" -ForegroundColor White
    Write-Host "`nTo check container status: docker ps" -ForegroundColor Yellow
    Write-Host "To stop containers: .\tools\down.ps1" -ForegroundColor Yellow
} else {
    Write-Host "`n=== Docker Compose Up Failed ===" -ForegroundColor Red
    exit 1
}
