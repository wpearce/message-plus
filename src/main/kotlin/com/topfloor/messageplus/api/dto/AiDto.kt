package com.topfloor.messageplus.api.dto

import java.util.UUID

data class AiDto(val id: UUID, val response: String)

fun AiDto.toDto() = AiDto(id = id, response = response)

data class AiInputDto(
    val prompt: String,
    val model: LLM?,
)

enum class LLM {
    GPT4o,
    GPT5
}