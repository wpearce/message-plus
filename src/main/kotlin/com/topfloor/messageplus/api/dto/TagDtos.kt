package com.topfloor.messageplus.api.dto

import com.topfloor.messageplus.domain.Tag
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class TagDto(val id: UUID, val name: String)

fun Tag.toDto() = TagDto(id = requireNotNull(id), name = name)

data class CreateTagRequest(
    @field:NotBlank
    @field:Size(max = 64)
    val name: String
)