package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.database.User.AGATHA
import com.toptal.ggurgul.timezones.functional.database.User.GREG
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
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
                    insertUser(this, GREG)
                    insertUser(this, AGATHA)
                }
                insertInto("USER_AUTHORITIES") {
                    assignAuthorityToUser(this, Authority.USER, GREG)
                    assignAuthorityToUser(this, Authority.MANAGER, AGATHA)
                }
                insertInto("TIMEZONES") {
                    insertTimezone(this, Timezone.KRAKOW, GREG)
                    insertTimezone(this, Timezone.TOKYO, GREG)
                    insertTimezone(this, Timezone.WARSAW, AGATHA)
                }
            }
        }

    }

    @Test
    @AuthenticatedAsUser(GREG)
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