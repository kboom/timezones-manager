package com.toptal.ggurgul.timezones.functional.tests.profile

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test


class ProfileTests : AbstractFunctionalTest() {

    @Before
    fun setUpDatabase() {
        prepareDatabase {
            insertInto("USERS") {
                insertUser(this, User.ALICE)
            }
            insertInto("USER_AUTHORITIES") {
                assignAuthorityToUser(this, Authority.USER, User.ALICE)
            }
        }
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCanChangePassword() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "oldPassword": "${User.ALICE.password}",
                    "newPassword": "abcdef9"
                }""".trimIndent())
                .post("/profile/password")
                .then()
                .statusCode(200)
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCanUpdateProfile() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "firstName": "Alice",
                    "lastName": "Hanks"
                }""".trimIndent())
                .put("/profile")
                .then()
                .statusCode(200)
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCannotChangePasswordIfWrongCurrentProvided() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "oldPassword": "wrong",
                    "newPassword": "abcdef9"
                }""".trimIndent())
                .post("/profile/password")
                .then()
                .statusCode(403)
    }

}