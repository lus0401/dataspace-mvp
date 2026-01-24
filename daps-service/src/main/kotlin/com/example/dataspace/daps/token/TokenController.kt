package com.example.dataspace.daps.token

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TokenController(
    private val tokenService: TokenService
) {

    @PostMapping("/token")
    fun token(@RequestBody req: TokenRequest): TokenResponse {
        return tokenService.issue(req)
    }
}
