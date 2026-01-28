# Dataspace MVP (Local)

본 프로젝트는 Dataspace 개념 검증을 위한 MVP로,
DAPS 기반 JWT 인증을 포함한 Consumer – Broker – Provider 데이터 접근 흐름을
Spring Boot 멀티모듈 구조로 구현한다.

## 1. Architecture Overview

DAPS: JWT 발급 (인증 서버)

Broker: Data Offer 등록 및 조회

Provider: 실제 데이터 제공 (JWT 검증 + Scope 기반 접근제어)

Consumer: Broker 조회 → DAPS 토큰 발급 → Provider 데이터 요청

```jsx
Consumer
   │
   │ (1) GET /offers
   ▼
Broker
   │
   │ (2) POST /token
   ▼
DAPS
   │
   │ (3) GET /data (Authorization: Bearer JWT)
   ▼
Provider
```

## 2. Project Structure

초기 MVP 단계에서는 단일 레포 기반 Gradle 멀티모듈 구조를 사용한다.
```jsx
dataspace_mvp
  ├─ daps-service/
  ├─ broker-service/
  ├─ provider-service/
  ├─ consumer-service/
  ├─ common/
  ├─ src/          ← ❗ 안 씀 (루트 모듈)
```

## 3.Dependencies

### daps-service

- ✅ Spring Web
- ✅ Spring Security
- ✅ JJWT (HS256 기반 JWT 발급)
    

### broker-service

- ✅ Spring Web
- ✅ Spring Data JPA
- ✅ PostgreSQL Driver
- ✅ Validation

### provider-service

- ✅ Spring Web
- ✅ Spring Security
- ✅ OAuth2 Resource Server (JWT 검증용)

### consumer-service

- ✅ Spring Web

# Settings

## 4.Ports

daps-service : 8081

broker-service : 8082

provider-service : 8083

consumer-service : 8084

## 5.Run (Local)

### Prerequisites
- JDK 17
- Docker Desktop (for PostgreSQL)

### Start PostgreSQL
```powershell
docker compose up -d
docker ps
```
### PostgreSQL info:
- host: localhost
- port: 5432
- db: dataspace
- user/password: dataspace / dataspace

### Run Services

In separate terminals (project root):
```powershell
.\gradlew :daps-service:bootRun
.\gradlew :broker-service:bootRun
.\gradlew :provider-service:bootRun
.\gradlew :consumer-service:bootRun
```

### Verify
```powershell
curl.exe http://localhost:8081/hello
curl.exe http://localhost:8082/hello
curl.exe http://localhost:8083/hello
curl.exe http://localhost:8084/hello

curl.exe http://localhost:8081/actuator/health
curl.exe http://localhost:8082/actuator/health
curl.exe http://localhost:8083/actuator/health
curl.exe http://localhost:8084/actuator/health
```

## 6. Functional Test Flow (MVP 핵심)
### Step 1. Broker에 Offer 등록
```powershell
$body = @{
  title       = "Provider Data Offer"
  description = "Fetch /data from provider"
  providerUrl = "http://localhost:8083"
  ownerId     = "provider-1"
  dataType    = "application/json"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8082/offers" `
  -ContentType "application/json" `
  -Body $body
```
### Step 2. Broker Offer 조회
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/offers"
```
### Step 3. Consumer 단일 호출 
```powershell
Invoke-RestMethod -Uri "http://localhost:8084/fetch"
```
**내부 동작 흐름**

1. Consumer → Broker /offers
2. Consumer → DAPS /token (JWT 발급)
3. Consumer → Provider /data
    - Authorization: Bearer <JWT>
4. Provider JWT 검증 + Scope(data:read) 확인
5. Provider 데이터 응답

## 7. Security Summary

- JWT Algorithm: HS256 (HMAC)
- Token Issuer: daps-service
- Protected Endpoint: provider-service /data
- Access Control
  - Authorization Header 필수
  - Scope: data:read

## 8. Current Status

- ✅ 멀티모듈 Gradle 기반 Dataspace MVP 구조 구성
- ✅ PostgreSQL 연동 (broker-service)
- ✅ Broker Offer 등록 / 조회 API 구현
- ✅ DAPS JWT 발급 엔드포인트 구현
- ✅ Provider JWT 검증 및 Scope 기반 접근제어
- ✅ Consumer 단일 호출로 전체 데이터 흐름 검증 완료

##  9. Next Steps (Planned)
- Token caching (expires_in 기반 재사용)
- Clearinghouse 서비스 추가 (데이터 접근 이벤트 로깅)
- Provider allowlist 정책 (clientId 기반 접근 제어)
- RS256 + JWKS 기반 DAPS 고도화
