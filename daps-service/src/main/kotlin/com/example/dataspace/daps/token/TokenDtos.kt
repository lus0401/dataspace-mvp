package com.example.dataspace.daps.token

data class TokenRequest(
    val clientId: String,
    val scope: String? = "data:read"
)

data class TokenResponse(
    val access_token: String,
    val token_type: String = "Bearer",
    val expires_in: Long
)
