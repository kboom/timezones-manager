package com.ggurgul.playground.timezones.functional.tests.authentication

import com.ggurgul.playground.timezones.functional.database.Authority
import com.ggurgul.playground.timezones.functional.database.User
import com.ggurgul.playground.timezones.functional.database.assignAuthorityToUser
import com.ggurgul.playground.timezones.functional.database.insertUser
import com.ggurgul.playground.timezones.functional.rules.AuthenticationRule
import com.ggurgul.playground.timezones.functional.rules.DataLoadingRule
import com.ggurgul.playground.timezones.functional.rules.ReadOnly
import com.ggurgul.playground.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.isA
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

internal class AuthenticationTests : AbstractFunctionalTest() {

    private val authenticationRule = AuthenticationRule()
    private val dataLoadingRule = DataLoadingRule({
        insertInto("USERS") {
            insertUser(this, User.GREG)
            insertUser(this, User.AGATHA)
            insertUser(this, User.ALICE)
            insertUser(this, User.KATE)
        }
        insertInto("USER_AUTHORITIES") {
            assignAuthorityToUser(this, Authority.ADMIN, User.GREG)
            assignAuthorityToUser(this, Authority.MANAGER, User.AGATHA)
            assignAuthorityToUser(this, Authority.USER, User.ALICE)
            assignAuthorityToUser(this, Authority.USER, User.KATE)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)
            .around(authenticationRule);

    @Test
    @ReadOnly
    fun userCannotObtainTokenIfNotEnabled() {
        given()
                .body("""
                    {
                        "username": "${User.KATE.username}",
                        "password": "${User.KATE.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    @ReadOnly
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
    @ReadOnly
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
    @ReadOnly
    fun userCanObtainUserToken() {
        given()
                .body("""
                    {
                        "username": "${User.ALICE.username}",
                        "password": "${User.ALICE.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    @ReadOnly
    fun managerCanObtainManagerToken() {
        given()
                .body("""
                    {
                        "username": "${User.AGATHA.username}",
                        "password": "${User.AGATHA.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    @ReadOnly
    fun adminCanObtainAdminToken() {
        given()
                .body("""
                    {
                        "username": "${User.GREG.username}",
                        "password": "${User.GREG.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

}