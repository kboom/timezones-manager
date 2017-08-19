package com.toptal.ggurgul.timezones.functional.authentication

import com.toptal.ggurgul.timezones.functional.AbstractFunctionalTest
import io.restassured.RestAssured.given
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import org.hamcrest.Matchers.isA
import org.junit.Test

internal class AuthenticationTests : AbstractFunctionalTest() {

    @Test
    fun userCannotObtainTokenIfInvalidUsername() {
        given()
                .body("""
                    {
                        "username": "invalid",
                        "password": "qwerty1"
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
                        "password": "qwerty1"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("authPost.json"))
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun managerCanObtainManagerToken() {

    }

    @Test
    fun adminCanObtainAdminToken() {

    }

}