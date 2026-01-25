package com.example.dataspace.clearinghouse.event

import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class TransferEventService(
    private val repo: TransferEventRepository
) {
    fun log(req: TransferEventRequest): TransferEventResponse {
        val entity = _root_ide_package_.com.example.dataspace.clearinghouse.event.TransferEventEntity(
            eventTime = req.eventTime ?: OffsetDateTime.now(),
            consumerId = req.consumerId,
            providerUrl = req.providerUrl,
            offerId = req.offerId,
            dataEndpoint = req.dataEndpoint,
            status = req.status,
            message = req.message
        )

        val saved = repo.save(entity)
        return saved.toResponse()
    }

    fun list(consumerId: String?, status: String?): List<TransferEventResponse> =
        repo.findFiltered(consumerId, status).map { it.toResponse() }

    private fun TransferEventEntity.toResponse() =
        _root_ide_package_.com.example.dataspace.clearinghouse.event.TransferEventResponse(
            id = requireNotNull(this.id),
            eventTime = this.eventTime,
            consumerId = this.consumerId,
            providerUrl = this.providerUrl,
            offerId = this.offerId,
            dataEndpoint = this.dataEndpoint,
            status = this.status,
            message = this.message
        )
}
