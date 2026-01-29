package com.example.dataspace.broker

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): Map<String, String> =
        mapOf(
            "service" to "broker-service",
            "status" to "ok"
        )
}
