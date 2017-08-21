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
    val authenticationRule = AuthenticationRule(AbstractFunctionalTest.httpClient)

    companion object {

        internal val httpClient = HttpClients.createDefault()

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080;
            RestAssured.requestSpecification = RequestSpecBuilder()
                    .setContentType("application/json")
                    .addFilter {
                        requestSpec, responseSpec, context ->
                            requestSpec.header("Authorization", "Bearer abc");
                            context.next(requestSpec, responseSpec)
                    }
                    .build()
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            httpClient.close()
        }

    }

}