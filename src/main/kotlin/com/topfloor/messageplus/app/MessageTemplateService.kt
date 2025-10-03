package com.topfloor.messageplus.app

import com.topfloor.messageplus.api.dto.CreateUpdateMessageTemplateDto
import com.topfloor.messageplus.api.dto.MessageTemplateDto
import com.topfloor.messageplus.api.dto.toDto
import com.topfloor.messageplus.domain.MessageTemplate
import com.topfloor.messageplus.domain.MessageTemplateRepository
import com.topfloor.messageplus.domain.MessageTemplateSpecs
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

@Service
class MessageTemplateService(
    private val repo: MessageTemplateRepository
) {

    @Transactional(readOnly = true)
    fun list(q: String?): List<MessageTemplate> =
        if (q.isNullOrBlank()) repo.findAll() else repo.findByTitleContainingIgnoreCase(q)

    @Transactional(readOnly = true)
    fun listByAllTagNames(tagNames: List<String>?, pageable: Pageable): Page<MessageTemplateDto> {
        val spec = if (tagNames.isNullOrEmpty()) null
        else MessageTemplateSpecs.hasAllTagNames(tagNames)
        val page = repo.findAll(spec, pageable)
        return page.map { entity -> entity.toDto() }
    }

    @Transactional(readOnly = true)
    fun listAllByAllTagNames(tagNames: List<String>?): List<MessageTemplateDto> {
        val spec = if (tagNames.isNullOrEmpty()) null
        else MessageTemplateSpecs.hasAllTagNames(tagNames)
        return repo.findAll(spec).map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): MessageTemplate =
        repo.findById(id).orElseThrow { EntityNotFoundException("Template $id not found") }

    @Transactional
    fun create(req: CreateUpdateMessageTemplateDto): MessageTemplate {
        if (repo.existsByTitleIgnoreCase(req.name)) {
            error("Template name already exists: ${req.name}")
        }
        return repo.save(MessageTemplate(title = req.name.trim(), bodyPt = req.bodyPt, bodyEn = req.bodyEn))
    }

    @Transactional
    fun update(id: UUID, req: CreateUpdateMessageTemplateDto): MessageTemplate {
        val template = get(id)
        // prevent duplicate names on other rows
        if (!template.title.equals(req.name, ignoreCase = true) && repo.existsByTitleIgnoreCase(req.name)) {
            error("Message Template name already exists: ${req.name}")
        }
        template.title = req.name.trim()
        template.bodyPt = req.bodyPt
        template.bodyEn = req.bodyEn
        return repo.save(template)
    }

    @Transactional
    fun delete(id: UUID) {
        if (!repo.existsById(id)) throw EntityNotFoundException("Template $id not found")
        repo.deleteById(id)
    }
}