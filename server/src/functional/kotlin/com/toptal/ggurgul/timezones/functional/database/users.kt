package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues
import java.util.*

fun insertGregUser(insertBuilder: Insert.Builder) {
    insertBuilder.mappedValues(
            "ID" to 100L,
            "USERNAME" to "greguser",
            "EMAIL" to "greg@test.com",
            "ENABLED" to true,
            "FIRSTNAME" to "Grzegorz",
            "LASTNAME" to "Gurgul",
            "PASSWORD" to "$2a$10$5XijhRl32k3/tBA1qJLao.GtH1j.EPM6/UnUsrq9R4wiOTXPLhnIW",
            "LAST_PWD_RST_DT" to Date()
    )
}