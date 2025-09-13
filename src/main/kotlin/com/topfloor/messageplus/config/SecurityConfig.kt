package com.topfloor.messageplus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }          // SPA + token-based auth
            .cors { }                       // keep your CORS allowlist
            .authorizeHttpRequests {
                it.requestMatchers("/actuator/health").permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt { } }  // expect Bearer JWTs
            .build()
}
