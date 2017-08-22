package com.toptal.ggurgul.timezones.functional.tests.authentication

import com.toptal.ggurgul.timezones.functional.database.Authority
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.database.assignAuthorityToUser
import com.toptal.ggurgul.timezones.functional.database.insertUser
import com.toptal.ggurgul.timezones.functional.database.prepareDatabase
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.isA
import org.junit.Before
import org.junit.Test

internal class AuthenticationTests : AbstractFunctionalTest() {

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
    fun userCannotObtainTokenIfInvalidUsername() {
        given()
                .body("""
                    {
                        "username": "invalid",
                        "password": "qwerty123"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    fun userCannotObtainTokenIfInvalidPassword() {
        given()
                .body("""
                    {
                        "username": "greg",
                        "password": "invalid"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    fun userCanObtainUserToken() {
        given()
                .body("""
                    {
                        "username": "${ALICE.username}",
                        "password": "${ALICE.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun managerCanObtainManagerToken() {
        given()
                .body("""
                    {
                        "username": "${AGATHA.username}",
                        "password": "${AGATHA.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun adminCanObtainAdminToken() {
        given()
                .body("""
                    {
                        "username": "${GREG.username}",
                        "password": "${GREG.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

}