package com.toptal.ggurgul.timezones.functional.tests.users

import com.toptal.ggurgul.timezones.functional.database.Authority
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.database.assignAuthorityToUser
import com.toptal.ggurgul.timezones.functional.database.insertUser
import com.toptal.ggurgul.timezones.functional.database.prepareDatabase
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.BeforeClass
import org.junit.Test


class UsersFunctionalTest : AbstractFunctionalTest() {

    companion object {

        @JvmStatic
        @BeforeClass
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
            }
        }

    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userCannotListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(403)
    }

    @Test
    @AuthenticatedAsUser(GREG)
    fun adminCanListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(200)
                .body("_embedded.users", hasSize<String>(equalTo(3)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun managerCanListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(200)
                .body("_embedded.users", hasSize<String>(equalTo(3)))
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}