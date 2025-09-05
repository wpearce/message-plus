package com.topfloor.messageplus.domain

import org.springframework.data.jpa.repository.JpaRepository

interface MessageTemplateRepository : JpaRepository<MessageTemplate, Long> {
    fun findByTitleContainingIgnoreCase(q: String): List<MessageTemplate>
    fun existsByTitleIgnoreCase(title: String): Boolean
}
