rootProject.name = "dataspace_mvp"

// Core 모듈
include("core:core-common")
include("core:core-domain")

// Support 모듈
include("support:support-logging")
include("support:support-web")

// Infra 모듈
include("infra:infra-persistence")

// Broker 서비스
include("service:broker:service-broker-domain")
include("service:broker:service-broker-application")
include("service:broker:service-broker-api")

// Clearinghouse 서비스
include("service:clearinghouse:service-clearinghouse-domain")
include("service:clearinghouse:service-clearinghouse-application")
include("service:clearinghouse:service-clearinghouse-api")

// Consumer 서비스
include("service:consumer:service-consumer-application")
include("service:consumer:service-consumer-api")

// Provider 서비스
include("service:provider:service-provider-api")

// DAPS 서비스
include("service:daps:service-daps-application")
include("service:daps:service-daps-api")
