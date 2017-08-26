package com.toptal.ggurgul.timezones.functional.tests.registration

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test


class RegistrationTests : AbstractFunctionalTest() {

    companion object {
        val CONFIRMATION_CODE_FOR_ANN = "a2F0ZTo2ZTVmMjJkZTQ5YzMzOTkwMDEyM2IyMjlmYTAwNjAwNQ=="
    }

    @Before
    fun setUpDatabase() {
        prepareDatabase {
            insertInto("USERS") {
                insertUser(this, User.KATE)
            }
            insertInto("USER_AUTHORITIES") {
                assignAuthorityToUser(this, Authority.USER, User.KATE)
            }
            insertInto("USER_CODES") {
                insertRegistrationConfirmationCode(this, User.KATE, CONFIRMATION_CODE_FOR_ANN)
            }
        }
    }

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

    @Test
    fun isUnauthorizedForWrongConfirmationCode() {
        dbSetupTracker.skipNextLaunch();
        RestAssured.given()
                .body("""
                    {
                        "code": "wrong"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(403)
    }

    @Test
    fun accountIsActivatedForValidConfirmationCode() {
        RestAssured.given()
                .body("""
                    {
                        "code": "$CONFIRMATION_CODE_FOR_ANN"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(200)
    }

}