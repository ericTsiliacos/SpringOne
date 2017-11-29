package io.pivotal.notes.models

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(unique = true)
    var username: String? = null

    @JsonIgnore
    var encryptedPassword: String? = null

    protected constructor() {}

    constructor(id: Int?, username: String, encryptedPassword: String) {
        this.id = id
        this.username = username
        this.encryptedPassword = encryptedPassword
    }
}
