package com.toptal.ggurgul.timezones.functional.authentication

import com.toptal.ggurgul.timezones.functional.AbstractFunctionalTest
import io.restassured.RestAssured.*
import io.restassured.matcher.RestAssuredMatchers.*
import org.hamcrest.Matchers.*
import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator.*
import org.junit.BeforeClass
import org.junit.Test

class AuthenticationTests : AbstractFunctionalTest() {

    @Test
    fun userCanObtainUserToken() {
        given()
                .body("""
                   "user": "greg",
                   "password": "qwerty1"
                """)
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