package com.example.dataspace.consumer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): Map<String, String> =
        mapOf(
            "service" to "consumer-service",
            "status" to "ok"
        )
}
