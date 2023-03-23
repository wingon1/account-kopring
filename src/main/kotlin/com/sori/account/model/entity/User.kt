package com.sori.account.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "user")
class User(
    @Column(name = "email", nullable = false, unique = true)
    var email: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @UpdateTimestamp
    @Column(name = "modified_at")
    var modifiedAt: Instant? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant? = null,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: Set<Role>? = null
): AbstractPersistence(){
}