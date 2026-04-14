package com.topfloor.messageplus.app

import com.topfloor.messageplus.api.dto.CreateUpdateMessageTemplateDto
import com.topfloor.messageplus.domain.MessageTemplate
import com.topfloor.messageplus.domain.MessageTemplateRepository
import com.topfloor.messageplus.domain.Tag
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class MessageTemplateServiceTest {

    private val repo = mockk<MessageTemplateRepository>()
    private val service = MessageTemplateService(repo)

    @Test
    fun `update preserves existing tag links`() {
        val id = UUID.randomUUID()
        val vipTag = Tag(id = UUID.randomUUID(), name = "vip")
        val template = MessageTemplate(
            id = id,
            title = "Initial title",
            bodyPt = "Texto",
            bodyEn = "Text"
        )
        template.tags.add(vipTag)

        every { repo.findById(id) } returns Optional.of(template)
        every { repo.save(any()) } answers { firstArg() }

        val updated = service.update(
            id = id,
            req = CreateUpdateMessageTemplateDto(
                title = "Updated title",
                bodyPt = "Novo texto",
                bodyEn = "New text"
            )
        )

        assertEquals(listOf("vip"), updated.tags.map { it.name })

        val savedTemplate = slot<MessageTemplate>()
        verify { repo.save(capture(savedTemplate)) }
        assertEquals(setOf(vipTag), savedTemplate.captured.tags)
    }

    @Test
    fun `update throws when template does not exist`() {
        val id = UUID.randomUUID()
        every { repo.findById(id) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            service.update(
                id = id,
                req = CreateUpdateMessageTemplateDto(
                    title = "Updated title",
                    bodyPt = "Novo texto",
                    bodyEn = "New text"
                )
            )
        }
    }
}
