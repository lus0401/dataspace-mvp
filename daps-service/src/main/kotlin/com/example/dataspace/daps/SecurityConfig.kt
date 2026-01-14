package com.example.dataspace.daps

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/hello",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()

                // MVP 단계에서는 전부 허용 (나중에 /token만 열어도 됨)
                it.anyRequest().permitAll()
            }

        return http.build()
    }
}
