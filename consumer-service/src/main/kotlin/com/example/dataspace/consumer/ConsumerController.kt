package com.example.dataspace.consumer

import com.example.dataspace.consumer.clearinghouse.ClearinghouseClient
import com.example.dataspace.consumer.daps.DapsClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class ConsumerController(
    private val restClient: RestClient,
    private val dapsClient: DapsClient,
    private val clearinghouseClient: ClearinghouseClient
) {
    // Broker의 OfferResponse와 동일 형태로 맞춤
    data class OfferResponse(
        val id: Long,
        val title: String,
        val description: String,
        val providerUrl: String,
        val ownerId: String,
        val dataType: String,
        val createdAt: String
    )

    @GetMapping("/search")
    fun search(@RequestParam(required = false) q: String?): List<OfferResponse> {
        val url = if (q.isNullOrBlank()) {
            "http://localhost:8082/offers"
        } else {
            "http://localhost:8082/offers?q=$q"
        }

        val arr = restClient.get()
            .uri(url)
            .retrieve()
            .body(Array<OfferResponse>::class.java)

        return arr?.toList() ?: emptyList()
    }

    @GetMapping("/fetch")
    fun fetch(@RequestParam(required = false) q: String?): Any {
        val offers = search(q)
        val selected = offers.firstOrNull()
            ?: return mapOf("error" to "no offers found", "query" to (q ?: ""))

        val providerDataUrl = selected.providerUrl.trimEnd('/') + "/data"

        return try {
            val token = dapsClient.issueToken()

            val providerResponse = restClient.get()
                .uri(providerDataUrl)
                .header("Authorization", "Bearer $token")
                .retrieve()
                .body(Any::class.java)

            // 성공 로깅
            try {
                clearinghouseClient.logSuccess(selected.providerUrl, selected.id)
            } catch (_: Exception) {
                // MVP 안정화를 위해 로깅 실패는 무시 (fetch는 성공 유지)
            }

            mapOf(
                "selectedOffer" to selected,
                "providerDataUrl" to providerDataUrl,
                "tokenIssuedBy" to "daps-service",
                "providerResponse" to providerResponse
            )
        } catch (e: Exception) {
            // 실패 로깅
            try {
                clearinghouseClient.logFailure(selected.providerUrl, selected.id, e.message)
            } catch (_: Exception) {
                // ignore
            }

            mapOf(
                "error" to "fetch failed",
                "providerDataUrl" to providerDataUrl,
                "message" to (e.message ?: e.javaClass.name)
            )
        }
    }


}
