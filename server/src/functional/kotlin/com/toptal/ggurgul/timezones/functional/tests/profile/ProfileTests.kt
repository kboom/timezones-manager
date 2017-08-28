package com.toptal.ggurgul.timezones.functional.tests.profile

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import com.toptal.ggurgul.timezones.functional.tests.registration.RegistrationTests
import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test


class ProfileTests : AbstractFunctionalTest() {

    companion object {
        val CONFIRMATION_CODE_FOR_AGATHA = "YWdhdGhhOjkxODQ0ZmMyYWFjOTQzZTcyMmQwZGNhNGMxZjk1OWNj"
    }

    @Before
    fun setUpDatabase() {
        prepareDatabase {
            insertInto("USERS") {
                insertUser(this, User.ALICE)
                insertUser(this, User.AGATHA)
            }
            insertInto("USER_AUTHORITIES") {
                assignAuthorityToUser(this, Authority.USER, User.ALICE)
                assignAuthorityToUser(this, Authority.USER, User.AGATHA)
            }
            insertInto("USER_CODES") {
                insertPasswordResetConfirmationCode(this, User.AGATHA, CONFIRMATION_CODE_FOR_AGATHA)
            }
        }
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCanGetProfile() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/profile")
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("alice"))
                .body("email", Matchers.equalTo("alice@test.com"))
                .body("firstName", Matchers.equalTo("Alice"))
                .body("lastName", Matchers.equalTo("Smith"))
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

    @Test
    fun canRequestPasswordReset() {
        RestAssured.given()
                .body("""{
                    "email": "${User.ALICE.email}"
                }""".trimIndent())
                .post("/profile/password/reset")
                .then()
                .statusCode(200)
    }

    @Test
    fun status200EvenIfEmailDoesNotExist() {
        RestAssured.given()
                .body("""{
                    "email": "blabla@inexistent.com"
                }""".trimIndent())
                .post("/profile/password/reset")
                .then()
                .statusCode(200)
    }

    @Test
    fun canSetNewPassword() {
        RestAssured.given()
                .body("""{
                    "newPassword": "blaa443",
                    "code": "$CONFIRMATION_CODE_FOR_AGATHA"
                }""".trimIndent())
                .post("/profile/password/reset/confirmation")
                .then()
                .statusCode(200)
    }

}