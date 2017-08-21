package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues


enum class Authority(
        val id: Long,
        val roleName: String
) {

    ADMIN(
            id = 1L,
            roleName = "ROLE_ADMIN"
    ),
    MANAGER(
            id = 2L,
            roleName = "ROLE_MANAGER"
    ),
    USER(
            id = 3L,
            roleName = "ROLE_USER"
    )

}


fun assignAuthorityToUser(insertBuilder: Insert.Builder, authority: Authority, user: User) {
    insertBuilder.mappedValues(
            "USER_ID" to user.id,
            "AUTHORITY_ID" to authority.id
    )
}

