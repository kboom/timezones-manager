package com.toptal.ggurgul.timezones.functional.tests.users

import com.toptal.ggurgul.timezones.functional.database.Authority
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.database.assignAuthorityToUser
import com.toptal.ggurgul.timezones.functional.database.insertUser
import com.toptal.ggurgul.timezones.functional.database.prepareDatabase
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test


class UsersTests : AbstractFunctionalTest() {

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

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun managerCanCreateUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "username": "alice2",
                    "password": "abc",
                    "email": "qwerty666",
                    "enabled": true,
                    "authorities": ["ROLE_USER"]
                }""".trimIndent())
                .post("/users")
                .then()
                .statusCode(201)
                .body("username", equalTo("alice2"))
                .body("email", equalTo("qwerty666"))
                .body("enabled", `is`(true))
                .body("lastPasswordResetDate", isEmptyOrNullString())
                .body("authorities", hasSize<Any>(1))
                .body("authorities[0].name", equalTo("ROLE_USER"))
    }

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun managerCanDeleteUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .delete("/users/${ALICE.id}")
                .then()
                .statusCode(204)
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun managerCanUpdateUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "username": "alice2",
                    "password": "abc",
                    "email": "qwerty666",
                    "enabled": true,
                    "lastPasswordResetDate": "2017-08-21T19:47:30.844+0000"
                }""".trimIndent())
                .put("/users/${ALICE.id}")
                .then()
                .statusCode(200)
//                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/timezones.json"))
    }

}