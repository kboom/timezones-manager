package com.toptal.ggurgul.timezones.domain.models.security

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "AUTHORITIES")
class Authority(
        roleName: AuthorityName? = null
) {

    @Id
    @Column(name = "AUTHORITY_NAME", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    var name: AuthorityName? = roleName

}