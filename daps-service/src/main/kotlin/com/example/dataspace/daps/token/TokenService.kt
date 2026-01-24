package com.example.dataspace.daps.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

@Service
class TokenService(
    @Value("\${daps.jwt.issuer}") private val issuer: String,
    @Value("\${daps.jwt.audience}") private val audience: String,
    @Value("\${daps.jwt.secret}") private val secret: String,
    @Value("\${daps.jwt.ttl-seconds}") private val ttlSeconds: Long
) {

    fun issue(req: TokenRequest): TokenResponse {
        val now = Instant.now()
        val exp = now.plusSeconds(ttlSeconds)

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setAudience(audience)
            .setSubject(req.clientId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(exp))
            .claim("scope", req.scope ?: "data:read")
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponse(
            access_token = jwt,
            expires_in = ttlSeconds
        )
    }
}
