package com.topfloor.messageplus.api

import com.topfloor.messageplus.api.dto.CreateTagRequest
import com.topfloor.messageplus.api.dto.TagDto
import com.topfloor.messageplus.api.dto.toDto
import com.topfloor.messageplus.app.TaggingService
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val taggingService: TaggingService
) {
    @GetMapping
    fun list() = taggingService.listAllTags().map { it.toDto() }

    @PostMapping
    fun create(@Valid @RequestBody req: CreateTagRequest): ResponseEntity<TagDto> {
        val saved = taggingService.create(req)
        return ResponseEntity.created(URI.create("/api/tags/${saved.id}")).body(saved.toDto())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) = taggingService.delete(id)


    @ExceptionHandler(EntityNotFoundException::class)
    fun notFound(ex: EntityNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to ex.message))

    @ExceptionHandler(IllegalStateException::class)
    fun conflict(ex: IllegalStateException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message))
}
