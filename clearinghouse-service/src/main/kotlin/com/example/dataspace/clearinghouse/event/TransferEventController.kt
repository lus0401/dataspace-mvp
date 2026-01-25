package com.example.dataspace.clearinghouse.event

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class TransferEventController(
    private val service: TransferEventService
) {

    @PostMapping
    fun create(@RequestBody req: TransferEventRequest): TransferEventResponse =
        service.log(req)

    @GetMapping
    fun list(
        @RequestParam(required = false) consumerId: String?,
        @RequestParam(required = false) status: String?
    ): List<TransferEventResponse> =
        service.list(consumerId, status)
}
