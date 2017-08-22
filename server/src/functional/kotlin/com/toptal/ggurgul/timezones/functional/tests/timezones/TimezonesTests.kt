package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test

class TimezonesTests : AbstractFunctionalTest() {

    @Before
    fun setUpDatabase() {
        prepareDatabase {
            insertInto("USERS") {
                insertUser(this, GREG)
                insertUser(this, AGATHA)
                insertUser(this, ALICE)
            }
            insertInto("USER_AUTHORITIES") {
                assignAuthorityToUser(this, Authority.ADMIN, GREG)
                assignAuthorityToUser(this, Authority.MANAGER, AGATHA)
                assignAuthorityToUser(this, Authority.USER, ALICE)
            }
            insertInto("TIMEZONES") {
                insertTimezone(this, Timezone.KRAKOW, GREG)
                insertTimezone(this, Timezone.TOKYO, GREG)
                insertTimezone(this, Timezone.WARSAW, AGATHA)
                insertTimezone(this, Timezone.SYDNEY, ALICE)
            }
        }
    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userSeesHisOwnTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(2)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun managerSeesHisOwnTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(2)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

    @Test
    @AuthenticatedAsUser(GREG)
    fun adminSeesAllTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(4)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userCanCreateTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "name": "Middle of nowhere",
                    "locationName": "Nowhere",
                    "differenceToGMT": 12
                }""".trimIndent())
                .post("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(2)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}