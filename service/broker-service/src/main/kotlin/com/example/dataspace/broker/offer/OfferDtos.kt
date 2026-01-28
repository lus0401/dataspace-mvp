package com.example.dataspace.broker.offer

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateOfferRequest(
    @field:NotBlank @field:Size(max = 200)
    val title: String,

    @field:NotBlank @field:Size(max = 2000)
    val description: String,

    @field:NotBlank @field:Size(max = 500)
    val providerUrl: String,

    @field:NotBlank @field:Size(max = 100)
    val ownerId: String,

    @field:NotBlank @field:Size(max = 100)
    val dataType: String = "application/json"
)

data class OfferResponse(
    val id: Long,
    val title: String,
    val description: String,
    val providerUrl: String,
    val ownerId: String,
    val dataType: String,
    val createdAt: String
)
