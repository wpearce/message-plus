package com.topfloor.messageplus.api

import com.ninjasquad.springmockk.MockkBean
import com.topfloor.messageplus.app.TaggingService
import com.topfloor.messageplus.config.SecurityConfig
import com.topfloor.messageplus.domain.Tag
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@WebMvcTest(TagController::class)
@Import(SecurityConfig::class)
class TagControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var service: TaggingService

    @MockkBean
    lateinit var jwtDecoder: JwtDecoder

    @Test
    fun `GET by id returns 200 for authenticated user without templates_write role`() {
        val id = UUID.randomUUID()
        val name1 = "first tag"
        val name2 = "second tag"

        every { service.listAllTags() } returns listOf(Tag(id= UUID.randomUUID(), name=name1), Tag(id=UUID.randomUUID(), name=name2))

        mockMvc.get("/api/tags") {
            accept = MediaType.APPLICATION_JSON
            with(SecurityMockMvcRequestPostProcessors.jwt())
        }
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].name") { value(name1) }
                jsonPath("$[1].name") { value(name2) }
            }
    }

    @Test
    fun `Create tag returns 403 when templates_write role is missing`() {
        mockMvc.post("/api/tags") {
            contentType = MediaType.APPLICATION_JSON
            with(SecurityMockMvcRequestPostProcessors.jwt())
        }
            .andExpect {
                status { isForbidden() }
            }

        verify(exactly = 0) { service.create(any()) }
    }

    @Test
    fun `Create template returns 201 when templates_write role is present`() {
        val id = UUID.randomUUID()
        val body = """
            {
                "name": "AVALIAÇÃO"
            }
        """.trimIndent()

        every { service.create(any()) } returns Tag(
            id,
            name = "AVALIAÇÃO"
        )

        mockMvc.post("/api/tags") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            with(
                SecurityMockMvcRequestPostProcessors.jwt().jwt { jwt ->
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
                header { string("Location", "/api/tags/$id") }
            }
    }
}
