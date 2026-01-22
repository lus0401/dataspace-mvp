package com.example.dataspace.consumer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class ConsumerController(
    private val restClient: RestClient
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

    /**
     * MVP: broker에서 offer를 조회하고,
     * (기본) 첫 번째 offer를 선택해서 providerUrl + "/data"를 호출한 결과를 반환
     */
    @GetMapping("/fetch")
    fun fetch(
        @RequestParam(required = false) q: String?
    ): Any {
        val offers = search(q)
        val selected = offers.firstOrNull()
            ?: return mapOf("error" to "no offers found", "query" to (q ?: ""))

        val providerDataUrl = selected.providerUrl.trimEnd('/') + "/data"

        val providerResponse = restClient.get()
            .uri(providerDataUrl)
            .retrieve()
            .body(Any::class.java)

        return mapOf(
            "selectedOffer" to selected,
            "providerDataUrl" to providerDataUrl,
            "providerResponse" to providerResponse
        )
    }
}
