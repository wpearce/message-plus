package com.topfloor.messageplus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.authorization.AuthorizationDecision

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }          // SPA + token-based auth
            .cors { }                       // keep your CORS allowlist
            .authorizeHttpRequests {
                it.requestMatchers("/actuator/health").permitAll()

                it.requestMatchers(HttpMethod.POST, "/api/templates/**")
                    .access { authentication, _ -> AuthorizationDecision(hasTemplatesWriteRole(authentication.get().principal)) }
                it.requestMatchers(HttpMethod.PUT, "/api/templates/**")
                    .access { authentication, _ -> AuthorizationDecision(hasTemplatesWriteRole(authentication.get().principal)) }
                it.requestMatchers(HttpMethod.DELETE, "/api/templates/**")
                    .access { authentication, _ -> AuthorizationDecision(hasTemplatesWriteRole(authentication.get().principal)) }

                it.requestMatchers(HttpMethod.POST, "/api/tags/**")
                    .access { authentication, _ -> AuthorizationDecision(hasTemplatesWriteRole(authentication.get().principal)) }
                it.requestMatchers(HttpMethod.DELETE, "/api/tags/**")
                    .access { authentication, _ -> AuthorizationDecision(hasTemplatesWriteRole(authentication.get().principal)) }

                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt { } }  // expect Bearer JWTs
            .build()

    private fun hasTemplatesWriteRole(principal: Any?): Boolean {
        val jwt = principal as? Jwt ?: return false
        val rolesClaim = jwt.claims[ROLES_CLAIM] as? Map<*, *> ?: return false
        val templatesWriteRole = rolesClaim[TEMPLATES_WRITE_ROLE] as? Map<*, *>
        return !templatesWriteRole.isNullOrEmpty()
    }

    companion object {
        private const val ROLES_CLAIM = "urn:zitadel:iam:org:project:337686608499219119:roles"
        private const val TEMPLATES_WRITE_ROLE = "templates_write"
    }
}
