package com.topfloor.messageplus.api.dto

import com.topfloor.messageplus.domain.MessageTemplate
import java.time.Instant
import java.util.UUID

data class MessageTemplateDto(
    val id: UUID,
    val title: String,
    val bodyPt: String,
    val bodyEn: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val tags: List<TagDto> = emptyList()
)

data class CreateUpdateMessageTemplateDto(
    val title: String,
    val bodyPt: String,
    val bodyEn: String
)

fun MessageTemplate.toDto() = MessageTemplateDto(
    id = requireNotNull(id),
    title = title,
    bodyPt = bodyPt,
    bodyEn = bodyEn,
    createdAt = createdAt,
    updatedAt = updatedAt,
    tags = tags.map { it.toDto() }.sortedBy { it.name }
)