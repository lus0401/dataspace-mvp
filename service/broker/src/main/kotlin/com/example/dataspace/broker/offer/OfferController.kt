package com.example.dataspace.broker.offer

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/offers")
class OfferController(
    private val offerService: OfferService
) {

    @PostMapping
    fun create(@Valid @RequestBody req: CreateOfferRequest): OfferResponse {
        return offerService.create(req)
    }

    @GetMapping
    fun list(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) ownerId: String?
    ): List<OfferResponse> {
        return offerService.list(q, ownerId)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): OfferResponse {
        return offerService.get(id)
    }
}
