package com.topfloor.messageplus.api.dto

import com.topfloor.messageplus.domain.MessageTemplate
import java.time.Instant

data class MessageTemplateDto(
    val id: Long,
    val title: String,
    val body: String,
    val createdAt: Instant?,
    val updatedAt: Instant?
)

data class CreateUpdateMessageTemplateDto(
    val name: String,
    val body: String
)

fun MessageTemplate.toDto() = MessageTemplateDto(
    id = requireNotNull(id),
    title = title,
    body = body,
    createdAt = createdAt,
    updatedAt = updatedAt
)