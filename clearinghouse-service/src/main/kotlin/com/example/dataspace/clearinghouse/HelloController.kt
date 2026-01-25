package com.example.dataspace.clearinghouse

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello() = mapOf("service" to "clearinghouse-service", "status" to "ok")
}
