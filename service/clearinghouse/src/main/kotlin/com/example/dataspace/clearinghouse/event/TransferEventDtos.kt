package com.example.dataspace.clearinghouse.event

import java.time.OffsetDateTime

data class TransferEventRequest(
    val consumerId: String,
    val providerUrl: String,
    val offerId: Long? = null,
    val dataEndpoint: String = "/data",
    val status: String = "SUCCESS",
    val message: String? = null,
    val eventTime: OffsetDateTime? = null
)

data class TransferEventResponse(
    val id: Long,
    val eventTime: OffsetDateTime,
    val consumerId: String,
    val providerUrl: String,
    val offerId: Long?,
    val dataEndpoint: String,
    val status: String,
    val message: String?
)
