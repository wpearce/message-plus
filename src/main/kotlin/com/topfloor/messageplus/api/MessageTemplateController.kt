package com.topfloor.messageplus.api

import com.topfloor.messageplus.api.dto.CreateUpdateMessageTemplateDto
import com.topfloor.messageplus.api.dto.MessageTemplateDto
import com.topfloor.messageplus.api.dto.toDto
import com.topfloor.messageplus.app.MessageTemplateService
import com.topfloor.messageplus.app.TaggingService
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/templates")
class MessageTemplateController(
    private val service: MessageTemplateService,
    private val taggingService: TaggingService
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) tagNames: List<String>?,
        @RequestParam(defaultValue = "true") paged: Boolean,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        return if (paged) {
            val page = service.listByAllTagNames(tagNames, pageable)
            ResponseEntity.ok(page)
        } else {
            val messages = service.listAllByAllTagNames(tagNames)
            ResponseEntity.ok(messages)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): MessageTemplateDto =
        service.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CreateUpdateMessageTemplateDto): ResponseEntity<MessageTemplateDto> {
        val saved = service.create(req)
        return ResponseEntity
            .created(URI.create("/api/templates/${saved.id}"))
            .body(saved.toDto())
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody req: CreateUpdateMessageTemplateDto): MessageTemplateDto =
        service.update(id, req).toDto()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) =
        service.delete(id)

    @ExceptionHandler(EntityNotFoundException::class)
    fun notFound(ex: EntityNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to ex.message))

    @ExceptionHandler(IllegalStateException::class)
    fun conflict(ex: IllegalStateException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message))

    @PostMapping("/{id}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addTag(@PathVariable id: UUID, @PathVariable tagId: UUID) {
        taggingService.addTag(id, tagId)
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeTag(@PathVariable id: UUID, @PathVariable tagId: UUID) {
        taggingService.removeTag(id, tagId)
    }
}
