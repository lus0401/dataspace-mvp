package com.example.dataspace.consumer

import com.example.dataspace.consumer.daps.DapsClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class ConsumerController(
    private val restClient: RestClient,
    private val dapsClient: DapsClient
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

            mapOf(
                "selectedOffer" to selected,
                "providerDataUrl" to providerDataUrl,
                "tokenIssuedBy" to "daps-service",
                "providerResponse" to providerResponse
            )
        } catch (e: Exception) {
            mapOf(
                "error" to "fetch failed",
                "providerDataUrl" to providerDataUrl,
                "message" to (e.message ?: e.javaClass.name)
            )
        }
    }


}
