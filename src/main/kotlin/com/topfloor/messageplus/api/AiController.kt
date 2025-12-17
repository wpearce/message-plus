package com.topfloor.messageplus.api

import com.fasterxml.jackson.core.JsonParseException
import com.topfloor.messageplus.api.dto.AiInputDto
import com.topfloor.messageplus.app.AiService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController(
    private val aiService: AiService,
) {
    @PostMapping("/improve")
    fun improve(@RequestBody req: AiInputDto) = aiService.improve(req)

    @PostMapping("/translate")
    fun translate(@RequestBody req: AiInputDto) = aiService.translate(req)

    @ExceptionHandler(JsonParseException::class)
    fun conflict(ex: IllegalStateException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error when parsing request body" to ex.message))
}
