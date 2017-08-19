package com.toptal.ggurgul.timezones.functional.tests.authentication

import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
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
                .body(matchesJsonSchemaInClasspath("schema/authPost.json"))
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    fun managerCanObtainManagerToken() {
        given()
                .body("""
                    {
                        "username": "manager",
                        "password": "manager"
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