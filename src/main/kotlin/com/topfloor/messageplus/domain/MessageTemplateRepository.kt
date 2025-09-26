package com.topfloor.messageplus.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageTemplateRepository : JpaRepository<MessageTemplate, UUID> {
    @EntityGraph(attributePaths = ["tags"])
    override fun findAll(): List<MessageTemplate>

    fun findByTitleContainingIgnoreCase(q: String): List<MessageTemplate>
    fun existsByTitleIgnoreCase(title: String): Boolean
}
