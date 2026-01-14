package com.example.dataspace.provider

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): Map<String, String> =
        mapOf(
            "service" to "provider-service",
            "status" to "ok"
        )
}