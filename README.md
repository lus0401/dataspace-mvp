# Structure

초기 MVP 단계에서는 단일 레포 기반의 Gradle 멀티모듈 구조

DAPS, Broker, Provider, Consumer를 각각 독립 Spring Boot 서비스로 분리해 구성

이후 단계에서 컨테이너 단위 분리를 고려한 구조

```jsx
dataspace_mvp
  ├─ daps-service/
  ├─ broker-service/
  ├─ provider-service/
  ├─ consumer-service/
  ├─ common/
  ├─ src/          ← ❗ 안 씀 (루트 모듈)
```

# Dependencies

### daps-service

- ✅ Spring Web
- ✅ Spring Security
    
    

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

## Ports

daps-service : 8081

broker-service : 8082

provider-service : 8083

consumer-service : 8084

# Run (Local)

## Prerequisites
- JDK 17
- Docker Desktop (for PostgreSQL)

## Start PostgreSQL
```powershell
docker compose up -d
docker ps
```
### PostgreSQL info:

host: localhost

port: 5432

db: dataspace

user/password: dataspace / dataspace

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

### Status

✅ Multi-module 구조 및 각 서비스 기동 확인

✅ Port 분리 및 기본 endpoint(/hello, /actuator/health) 구성

✅ broker-service PostgreSQL 연결 설정 완료
