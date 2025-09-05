package com.topfloor.messageplus.app

import com.topfloor.messageplus.api.dto.CreateUpdateMessageTemplateDto
import com.topfloor.messageplus.domain.MessageTemplate
import com.topfloor.messageplus.domain.MessageTemplateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityNotFoundException

@Service
class MessageTemplateService(
    private val repo: MessageTemplateRepository
) {

    @Transactional(readOnly = true)
    fun list(q: String?): List<MessageTemplate> =
        if (q.isNullOrBlank()) repo.findAll() else repo.findByTitleContainingIgnoreCase(q)

    @Transactional(readOnly = true)
    fun get(id: Long): MessageTemplate =
        repo.findById(id).orElseThrow { EntityNotFoundException("Template $id not found") }

    @Transactional
    fun create(req: CreateUpdateMessageTemplateDto): MessageTemplate {
        if (repo.existsByTitleIgnoreCase(req.name)) {
            error("Template name already exists: ${req.name}")
        }
        return repo.save(MessageTemplate(title = req.name.trim(), body = req.body))
    }

    @Transactional
    fun update(id: Long, req: CreateUpdateMessageTemplateDto): MessageTemplate {
        val template = get(id)
        // prevent duplicate names on other rows
        if (!template.title.equals(req.name, ignoreCase = true) && repo.existsByTitleIgnoreCase(req.name)) {
            error("Message Template name already exists: ${req.name}")
        }
        template.title = req.name.trim()
        template.body = req.body
        return repo.save(template)
    }

    @Transactional
    fun delete(id: Long) {
        if (!repo.existsById(id)) throw EntityNotFoundException("Template $id not found")
        repo.deleteById(id)
    }
}