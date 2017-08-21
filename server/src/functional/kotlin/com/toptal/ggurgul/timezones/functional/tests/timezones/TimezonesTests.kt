package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAs
import com.toptal.ggurgul.timezones.functional.rules.AuthenticationRule
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import io.restassured.module.jsv.JsonSchemaValidator
import org.apache.http.impl.client.HttpClients
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class TimezonesTests : AbstractFunctionalTest() {

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUpDatabase() {
            prepareDatabase {
                insertInto("USERS") {
                    insertUser(this, User.GREG)
                    insertUser(this, User.AGATHA)
                }
                insertInto("USER_AUTHORITIES") {
                    assignUserAuthority(this, User.GREG.id)
                    assignManagerAuthority(this, User.AGATHA.id)
                }
            }
        }

    }

    @Test
    @AuthenticatedAs("greg", "qwerty123")
    fun canGetTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}