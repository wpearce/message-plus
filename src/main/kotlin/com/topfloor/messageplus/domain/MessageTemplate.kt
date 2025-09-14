package com.topfloor.messageplus.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "message_template")
class MessageTemplate(
    @Id
    @GeneratedValue                 // tell Hibernate it's generated
    @UuidGenerator                  // Hibernate’s UUID generator
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 120)
    var title: String,

    @Column(nullable = false, columnDefinition = "text")
    var bodyPt: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null
)
