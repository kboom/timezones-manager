package com.toptal.ggurgul.timezones.functional

import io.restassured.RestAssured
import org.junit.BeforeClass

abstract class AbstractFunctionalTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080;
        }
    }

}