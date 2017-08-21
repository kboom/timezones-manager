package com.toptal.ggurgul.timezones.functional.tests.authentication

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import org.hamcrest.Matchers.isA
import org.junit.BeforeClass
import org.junit.Test

internal class AuthenticationTests : AbstractFunctionalTest() {

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
                    assignAuthorityToUser(this, Authority.USER, User.GREG)
                    assignAuthorityToUser(this, Authority.USER, User.AGATHA)
                }
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
                        "username": "greg",
                        "password": "qwerty123"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/authPost.json"))
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun managerCanObtainManagerToken() {
        given()
                .body("""
                    {
                        "username": "agatha",
                        "password": "qwerty321"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/authPost.json"))
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun adminCanObtainAdminToken() {
        given()
                .body("""
                    {
                        "username": "admin",
                        "password": "admin"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/authPost.json"))
                .and()
                .body("token", isA(String::class.java))
    }

}