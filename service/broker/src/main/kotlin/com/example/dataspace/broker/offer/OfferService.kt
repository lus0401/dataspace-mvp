package com.example.dataspace.broker.offer

import org.springframework.stereotype.Service

@Service
class OfferService(
    private val offerRepository: OfferRepository
) {

    fun create(req: CreateOfferRequest): OfferResponse {
        val saved = offerRepository.save(
            OfferEntity(
                title = req.title.trim(),
                description = req.description.trim(),
                providerUrl = req.providerUrl.trim(),
                ownerId = req.ownerId.trim(),
                dataType = req.dataType.trim()
            )
        )
        return saved.toResponse()
    }

    fun list(q: String?, ownerId: String?): List<OfferResponse> {
        val qq = q?.trim()?.takeIf { it.isNotBlank() }?.lowercase()
        val oo = ownerId?.trim()?.takeIf { it.isNotBlank() }

        return offerRepository.findAll()
            .asSequence()
            .filter { entity ->
                val okQ = qq == null ||
                        entity.title.lowercase().contains(qq) ||
                        entity.description.lowercase().contains(qq)

                val okOwner = oo == null || entity.ownerId == oo

                okQ && okOwner
            }
            .sortedByDescending { it.createdAt }
            .map { it.toResponse() }
            .toList()
    }


    fun get(id: Long): OfferResponse {
        val entity = offerRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Offer not found: $id") }
        return entity.toResponse()
    }

    private fun OfferEntity.toResponse(): OfferResponse =
        OfferResponse(
            id = requireNotNull(this.id),
            title = this.title,
            description = this.description,
            providerUrl = this.providerUrl,
            ownerId = this.ownerId,
            dataType = this.dataType,
            createdAt = this.createdAt.toString()
        )
}
