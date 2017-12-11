package com.ggurgul.playground.timezones.domain.models.security

import javax.persistence.*

@Entity
@Table(name = "AUTHORITIES")
class Authority {

    @Id
    @Column(name = "AUTHORITY_NAME")
    @Enumerated(EnumType.STRING)
    var name: AuthorityName? = null

}