package com.topfloor.messageplus.api

import com.topfloor.messageplus.api.dto.toDto
import com.topfloor.messageplus.app.TaggingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val taggingService: TaggingService
) {
    @GetMapping
    fun list() = taggingService.listAllTags().map { it.toDto() }
}
