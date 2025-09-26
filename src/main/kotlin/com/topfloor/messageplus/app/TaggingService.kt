package com.topfloor.messageplus.app

import com.topfloor.messageplus.domain.MessageTemplateRepository
import com.topfloor.messageplus.domain.Tag
import com.topfloor.messageplus.domain.TagRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TaggingService(
    private val messageTemplateRepo: MessageTemplateRepository,
    private val tagRepo: TagRepository
) {

    @Transactional(readOnly = true)
    fun listAllTags(): List<Tag> = tagRepo.findAll().sortedBy { it.name }


    @Transactional
    fun addTag(messageId: UUID, tagId: UUID): List<Tag> {
        val message = messageTemplateRepo.findById(messageId)
            .orElseThrow { EntityNotFoundException("Message $messageId not found") }
        val tag = tagRepo.findById(tagId)
            .orElseThrow { EntityNotFoundException("Tag $tagId not found") }

        message.tags.add(tag)
        messageTemplateRepo.save(message)
        return message.tags.sortedBy { it.name }
    }

    @Transactional
    fun removeTag(messageId: UUID, tagId: UUID): List<Tag> {
        val message = messageTemplateRepo.findById(messageId)
            .orElseThrow { EntityNotFoundException("Message $messageId not found") }
        message.tags.removeIf { it.id == tagId }
        messageTemplateRepo.save(message)
        return message.tags.sortedBy { it.name }
    }
}
