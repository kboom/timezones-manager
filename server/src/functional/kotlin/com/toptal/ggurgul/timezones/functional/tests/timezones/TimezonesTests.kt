package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import io.restassured.module.jsv.JsonSchemaValidator
import org.junit.BeforeClass
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
    fun canGetTimezones() {
        given()
                .header("Authorization", "Bearer " + "abc")
                .get("/timezones")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}