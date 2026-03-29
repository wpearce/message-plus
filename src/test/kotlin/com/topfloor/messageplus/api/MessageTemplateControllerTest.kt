package com.topfloor.messageplus.api

import com.ninjasquad.springmockk.MockkBean
import com.topfloor.messageplus.api.dto.MessageTemplateDto
import com.topfloor.messageplus.app.MessageTemplateService
import com.topfloor.messageplus.app.TaggingService
import com.topfloor.messageplus.config.SecurityConfig
import com.topfloor.messageplus.domain.MessageTemplate
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.util.UUID

@WebMvcTest(MessageTemplateController::class)
@Import(SecurityConfig::class)
class MessageTemplateControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var service: MessageTemplateService

    @MockkBean
    lateinit var taggingService: TaggingService

    @MockkBean
    lateinit var jwtDecoder: JwtDecoder

    @Test
    fun `GET by id returns 200 for authenticated user without templates_write role`() {
        val id = UUID.randomUUID()
        val now = Instant.parse("2025-09-01T12:00:00Z")

        every { service.get(id) } returns MessageTemplateDto(
            id = id,
            title = "ThankYou",
            bodyPt = "Obrigado por nos escolher!",
            bodyEn = "Thanks for choosing us!",
            createdAt = now,
            updatedAt = now
        )

        mockMvc.get("/api/templates/$id") {
            accept = MediaType.APPLICATION_JSON
            with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt())
        }
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id.toString()) }
                jsonPath("$.title") { value("ThankYou") }
            }
    }

    @Test
    fun `POST template returns 403 when templates_write role is missing`() {
        val body = """
            {
              "title": "Welcome",
              "bodyPt": "Olá",
              "bodyEn": "Hello"
            }
        """.trimIndent()

        mockMvc.post("/api/templates") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt())
        }
            .andExpect {
                status { isForbidden() }
            }

        verify(exactly = 0) { service.create(any()) }
    }

    @Test
    fun `POST template returns 201 when templates_write role is present`() {
        val id = UUID.randomUUID()
        val body = """
            {
              "title": "Welcome",
              "bodyPt": "Olá",
              "bodyEn": "Hello"
            }
        """.trimIndent()

        every { service.create(any()) } returns MessageTemplate(
            id = id,
            title = "Welcome",
            bodyPt = "Olá",
            bodyEn = "Hello"
        )

        mockMvc.post("/api/templates") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            with(
                org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt().jwt { jwt ->
                    jwt.claim(
                        "urn:zitadel:iam:org:project:337686608499219119:roles",
                        mapOf(
                            "templates_write" to mapOf("337686283054848687" to "topfloor.us1.zitadel.cloud")
                        )
                    )
                }
            )
        }
            .andExpect {
                status { isCreated() }
                header { string("Location", "/api/templates/$id") }
            }
    }
}
