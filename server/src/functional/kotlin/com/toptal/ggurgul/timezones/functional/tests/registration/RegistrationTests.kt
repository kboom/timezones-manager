package com.toptal.ggurgul.timezones.functional.tests.registration

import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.junit.Test


class RegistrationTests : AbstractFunctionalTest() {

    @Test
    fun userCanRegister() {
        RestAssured.given()
                .body("""
                    {
                        "username": "peter",
                        "password": "peterPan123",
                        "email": "gurgul.grzegorz@gmail.com"
                    }
                """.trim())
                .post("/registration")
                .then()
                .statusCode(200)
    }

}