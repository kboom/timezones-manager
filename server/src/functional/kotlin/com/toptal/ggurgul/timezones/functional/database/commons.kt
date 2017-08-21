package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.ninja_squad.dbsetup_kotlin.DbSetupBuilder
import com.ninja_squad.dbsetup_kotlin.dbSetup

fun prepareDatabase(configure: DbSetupBuilder.() -> Unit) {
    val url = System.getProperty("DB_URL", "jdbc:postgresql://localhost:5432/test")
    val user = System.getProperty("DB_USER", "test")
    val password = System.getProperty("DB_PASSWORD", "test")

    dbSetup(to = DriverManagerDestination(url, user, password)) {
        deleteAllFrom("TIMEZONES", "USER_AUTHORITIES", "USERS")
        configure()
    }.launch()
}