package com.example.dataspace.daps

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): Map<String, String> =
        mapOf(
            "service" to "daps-service",
            "status" to "ok"
        )
}
