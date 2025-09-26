package com.topfloor.messageplus.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TagRepository : JpaRepository<Tag, UUID> {
    fun findByNameIgnoreCase(name: String): Tag?
}