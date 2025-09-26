package com.topfloor.messageplus.domain

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "tag")
class Tag(
    @Id
    @GeneratedValue                 // tell Hibernate it's generated
    @UuidGenerator                  // Hibernate’s UUID generator
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 64)
    var name: String,

    @ManyToMany(mappedBy = "tags")
    var templates: MutableSet<MessageTemplate> = linkedSetOf()
)
