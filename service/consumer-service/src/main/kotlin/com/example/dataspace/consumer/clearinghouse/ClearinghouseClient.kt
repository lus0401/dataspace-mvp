package com.example.dataspace.consumer.clearinghouse

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime

@Component
class ClearinghouseClient(
    private val restClient: RestClient,
    @Value("\${dataspace.clearinghouse.base-url}") private val baseUrl: String,
    @Value("\${dataspace.daps.client-id}") private val consumerId: String
) {
    data class TransferEventRequest(
        val consumerId: String,
        val providerUrl: String,
        val offerId: Long?,
        val dataEndpoint: String,
        val status: String,
        val message: String?,
        val eventTime: OffsetDateTime? = null
    )

    fun logSuccess(providerUrl: String, offerId: Long?) {
        restClient.post()
            .uri("${baseUrl.trimEnd('/')}/events")
            .body(
                TransferEventRequest(
                    consumerId = consumerId,
                    providerUrl = providerUrl,
                    offerId = offerId,
                    dataEndpoint = "/data",
                    status = "SUCCESS",
                    message = null,
                    eventTime = OffsetDateTime.now()
                )
            )
            .retrieve()
            .toBodilessEntity()
    }

    fun logFailure(providerUrl: String, offerId: Long?, message: String?) {
        restClient.post()
            .uri("${baseUrl.trimEnd('/')}/events")
            .body(
                TransferEventRequest(
                    consumerId = consumerId,
                    providerUrl = providerUrl,
                    offerId = offerId,
                    dataEndpoint = "/data",
                    status = "FAIL",
                    message = message,
                    eventTime = OffsetDateTime.now()
                )
            )
            .retrieve()
            .toBodilessEntity()
    }
}
