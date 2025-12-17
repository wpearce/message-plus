package com.topfloor.messageplus.api

import com.topfloor.messageplus.app.AiService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController(
    private val aiService: AiService,
) {
    @GetMapping
    fun get() = aiService.get()

    @ExceptionHandler(IllegalStateException::class)
    fun conflict(ex: IllegalStateException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message))
}
