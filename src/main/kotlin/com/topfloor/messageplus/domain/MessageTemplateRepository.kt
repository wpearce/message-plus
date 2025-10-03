package com.topfloor.messageplus.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageTemplateRepository : JpaRepository<MessageTemplate, UUID> {
    fun findAll(spec: Specification<MessageTemplate>?): List<MessageTemplate>

    fun findAll(spec: Specification<MessageTemplate>?, pageable: Pageable): Page<MessageTemplate>

    fun findByTitleContainingIgnoreCase(q: String): List<MessageTemplate>
    fun existsByTitleIgnoreCase(title: String): Boolean
}
