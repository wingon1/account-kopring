package com.sori.account.model.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable

@MappedSuperclass
abstract class AbstractPersistence(
    @Id
    @GenericGenerator(name = "id", strategy = "com.sori.account.common.generator.SequenceGenerator")
    @GeneratedValue(generator = "id")
    @Column(nullable = false, updatable = false, columnDefinition = "bigint unsigned")
    open var id: Long? = null,
) : Serializable {
    fun isNew(): Boolean {
        return id == null
    }

    override fun hashCode(): Int {
        return id?.toInt()?.and(31) ?: 0
    }

    override fun equals(other: Any?): Boolean {
        return this.id?.equals(
            if (other is AbstractPersistence) {
                other.id
            } else {
                return false
            }
        ) ?: false
    }
}
