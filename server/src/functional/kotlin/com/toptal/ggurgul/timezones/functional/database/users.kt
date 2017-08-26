package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*


enum class User(
        val id: Long,
        val username: String,
        val email: String,
        val enabled: Boolean = true,
        val password: String,
        val lastPasswordResetDate: Date = Date()
) {

    GREG(
            id = 100L,
            username = "greg",
            password = "qwerty123",
            email = "greg@test.com"
    ),
    AGATHA(
            id = 101L,
            username = "agatha",
            password = "qwerty321",
            email = "agatha@test.com"
    ),
    ALICE(
            id = 102L,
            username = "alice",
            password = "qwerty666",
            email = "alice@test.com"
    ),
    KATE(
            id = 103L,
            username = "kate",
            password = "test123",
            email = "kate@test.com",
            enabled = false
    )

}

private val passwordEncoder = BCryptPasswordEncoder()

fun insertUser(insertBuilder: Insert.Builder, user: User) {
    insertBuilder.mappedValues(
            "ID" to user.id,
            "USERNAME" to user.username,
            "EMAIL" to user.email,
            "ENABLED" to user.enabled,
            "PASSWORD" to passwordEncoder.encode(user.password),
            "LAST_PWD_RST_DT" to user.lastPasswordResetDate
    )
}