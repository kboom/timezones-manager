package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues

fun assignUserAuthority(insertBuilder: Insert.Builder, userId: Long) {
    insertBuilder.mappedValues(
            "USER_ID" to userId,
            "AUTHORITY_ID" to 1L

    )
}

fun assignManagerAuthority(insertBuilder: Insert.Builder, userId: Long) {
    insertBuilder.mappedValues(
            "USER_ID" to userId,
            "AUTHORITY_ID" to 2L

    )
}