package com.toptal.ggurgul.timezones.functional.rules

import com.ninja_squad.dbsetup.DbSetupTracker
import com.ninja_squad.dbsetup.destination.DriverManagerDestination
import com.ninja_squad.dbsetup_kotlin.DbSetupBuilder
import com.ninja_squad.dbsetup_kotlin.dbSetup
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DataLoadingRule(
        private val configure: DbSetupBuilder.() -> Unit
) : TestRule {

    val dbSetupTracker = DbSetupTracker()

    fun prepareDatabase(configure: DbSetupBuilder.() -> Unit) {
        val url = System.getProperty("DB_URL", "jdbc:postgresql://localhost:5432/test")
        val user = System.getProperty("DB_USER", "test")
        val password = System.getProperty("DB_PASSWORD", "test")

        dbSetupTracker.launchIfNecessary(dbSetup(to = DriverManagerDestination(url, user, password)) {
            deleteAllFrom("USER_CODES", "TIMEZONES", "USER_AUTHORITIES", "USERS")
            configure()
        })
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            prepareDatabase(configure);
            base.evaluate()
        }
    }

}