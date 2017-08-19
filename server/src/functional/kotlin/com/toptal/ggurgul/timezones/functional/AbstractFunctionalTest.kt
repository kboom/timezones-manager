package com.toptal.ggurgul.timezones.functional

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.BeforeClass

abstract class AbstractFunctionalTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080;
            RestAssured.requestSpecification = RequestSpecBuilder()
                    .setContentType("application/json").build()
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

}