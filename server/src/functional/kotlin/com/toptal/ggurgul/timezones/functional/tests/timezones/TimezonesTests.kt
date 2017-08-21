package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAs
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.*
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
                insertInto("TIMEZONES") {
                    insertTimezone(this, Timezone.KRAKOW, User.GREG)
                    insertTimezone(this, Timezone.TOKYO, User.GREG)
                    insertTimezone(this, Timezone.WARSAW, User.AGATHA)
                }
            }
        }

    }

    @Test
    @AuthenticatedAs("greg", "qwerty123")
    fun userCanListHisOwnTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(2)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}