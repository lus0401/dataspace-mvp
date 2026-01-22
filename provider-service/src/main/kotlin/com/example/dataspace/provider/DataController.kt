package com.example.dataspace.provider

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
class DataController {

    @GetMapping("/data")
    fun data(): Map<String, Any> {
        return mapOf(
            "provider" to "provider-service",
            "timestamp" to OffsetDateTime.now().toString(),
            "payload" to mapOf(
                "message" to "hello from provider",
                "value" to 42,
                "items" to listOf("A", "B", "C")
            )
        )
    }
}
