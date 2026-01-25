package com.example.dataspace.consumer.daps

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class DapsClient(
    private val restClient: RestClient,
    @Value("\${dataspace.daps.base-url}") private val dapsBaseUrl: String,
    @Value("\${dataspace.daps.client-id}") private val clientId: String,
    @Value("\${dataspace.daps.scope}") private val scope: String
) {
    data class TokenRequest(
        val clientId: String,
        val scope: String
    )

    data class TokenResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: Long
    )

    fun issueToken(): String {
        val resp = restClient.post()
            .uri("${dapsBaseUrl.trimEnd('/')}/token")
            .body(TokenRequest(clientId = clientId, scope = scope))
            .retrieve()
            .body(TokenResponse::class.java)
            ?: throw IllegalStateException("DAPS token response is null")

        return resp.access_token
    }
}
