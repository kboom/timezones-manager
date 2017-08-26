package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.generator.ValueGenerators
import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType

fun insertRegistrationConfirmationCode(insertBuilder: Insert.Builder, user: User, code: String) {
    insertBuilder.withGeneratedValue("ID", ValueGenerators.sequence()
            .startingAt(1000L).incrementingBy(10))
    insertBuilder.mappedValues(
            "USER_ID" to user.id,
            "SENT_TO" to user.email,
            "TYPE" to UserCodeType.REGISTRATION_CONFIRMATION.name,
            "USER_ID" to user.id,
            "CODE" to code
    )
}