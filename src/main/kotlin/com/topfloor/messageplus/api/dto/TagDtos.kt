package com.topfloor.messageplus.api.dto

import com.topfloor.messageplus.domain.Tag
import java.util.UUID

data class TagDto(val id: UUID, val name: String)

fun Tag.toDto() = TagDto(id = requireNotNull(id), name = name)