package com.topfloor.messageplus.api

import com.topfloor.messageplus.api.dto.TemplateDto

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/templates")
class TemplateController {

    @GetMapping
    fun getAllTemplates(): List<TemplateDto> {
        return listOf(
            TemplateDto(1, "Welcome", "Hello and welcome to our service!"),
            TemplateDto(2, "Reminder", "Don’t forget your appointment tomorrow."),
            TemplateDto(3, "ThankYou", "Thanks for choosing us!")
        )
    }
}
