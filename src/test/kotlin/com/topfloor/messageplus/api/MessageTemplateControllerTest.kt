package com.topfloor.messageplus.api

import com.ninjasquad.springmockk.MockkBean
import com.topfloor.messageplus.app.MessageTemplateService
import com.topfloor.messageplus.domain.MessageTemplate
import io.mockk.every
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant

@WebMvcTest(MessageTemplateController::class)
class MessageTemplateControllerTest(
) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var service: MessageTemplateService

    @Test
    fun `GET by id returns 200 with dto`() {
        val now = Instant.parse("2025-09-01T12:00:00Z")

        val entity = MessageTemplate(
            id = 1L,
            title = "ThankYou",
            body = "Thanks for choosing us!",
            createdAt = now,
            updatedAt = now
        )

        every { service.get(1L) } returns entity

        mockMvc.get("/api/templates/1") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(1) }
                jsonPath("$.title") { value("ThankYou") }
                jsonPath("$.body") { value("Thanks for choosing us!") }
            }
    }

    @Test
    fun `GET by id returns 404 with error body when not found`() {
        every { service.get(99L) } throws EntityNotFoundException("MessageTemplate 99 not found")

        mockMvc.get("/api/templates/99") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isNotFound() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.error") { value(org.hamcrest.Matchers.containsString("99")) }
            }
    }
}
