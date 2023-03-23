package com.sori.account.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "roles")
class Role(
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @UpdateTimestamp
    @Column(name = "modified_at")
    var modifiedAt: Instant? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant? = null,
) : AbstractPersistence() {
}