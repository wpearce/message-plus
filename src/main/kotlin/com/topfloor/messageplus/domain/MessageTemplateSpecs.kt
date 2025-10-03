package com.topfloor.messageplus.domain

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object MessageTemplateSpecs {

    fun hasAllTagNames(requiredNames: Collection<String>): Specification<MessageTemplate> {
        val names = requiredNames.map { it.trim().lowercase() }.filter { it.isNotEmpty() }
        if (names.isEmpty()) return Specification.unrestricted()

        return Specification { root, query, cb ->
            val tagsJoin = root.join<MessageTemplate, Tag>("tags", JoinType.INNER)
            val where = cb.lower(tagsJoin.get<String>("name")).`in`(names)
            query?.groupBy(root.get<UUID>("id"))

            val distinctCount = cb.countDistinct(cb.lower(tagsJoin.get("name")))
            val having = cb.equal(distinctCount, names.size.toLong())

            query?.having(having)
            where
        }
    }
}
