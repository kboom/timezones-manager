package com.toptal.ggurgul.timezones.functional.tests

import com.toptal.ggurgul.timezones.functional.FunctionalTest
import com.toptal.ggurgul.timezones.functional.rules.AuthenticationRule
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.apache.http.impl.client.HttpClients
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.experimental.categories.Category

@Category(FunctionalTest::class)
abstract class AbstractFunctionalTest {

    @get:Rule
    val authenticationRule = AuthenticationRule()

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080;
            RestAssured.basePath = "/api"
            RestAssured.requestSpecification = RequestSpecBuilder()
                    .setContentType("application/json")
                    .setAccept("application/json")
                    .build()
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }

    }

}