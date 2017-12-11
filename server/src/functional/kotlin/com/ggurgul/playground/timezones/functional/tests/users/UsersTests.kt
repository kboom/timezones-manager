package com.ggurgul.playground.timezones.functional.tests.users

import com.ggurgul.playground.timezones.functional.database.Authority
import com.ggurgul.playground.timezones.functional.database.User
import com.ggurgul.playground.timezones.functional.database.assignAuthorityToUser
import com.ggurgul.playground.timezones.functional.database.insertUser
import com.ggurgul.playground.timezones.functional.rules.AuthenticatedAsUser
import com.ggurgul.playground.timezones.functional.rules.AuthenticationRule
import com.ggurgul.playground.timezones.functional.rules.DataLoadingRule
import com.ggurgul.playground.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule


class UsersTests : AbstractFunctionalTest() {

    private val authenticationRule = AuthenticationRule()
    private val dataLoadingRule = DataLoadingRule({
        insertInto("USERS") {
            insertUser(this, User.GREG)
            insertUser(this, User.AGATHA)
            insertUser(this, User.ALICE)
        }
        insertInto("USER_AUTHORITIES") {
            assignAuthorityToUser(this, Authority.ADMIN, User.GREG)
            assignAuthorityToUser(this, Authority.MANAGER, User.AGATHA)
            assignAuthorityToUser(this, Authority.USER, User.ALICE)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)
            .around(authenticationRule);

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCanGetHimselfByUsername() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .queryParam("username", User.ALICE.username)
                .get("/users/search/findByUsername?projections=withDetails")
                .then()
                .statusCode(200)
                .body("username", equalTo("alice"))
                .body("firstName", equalTo("Alice"))
                .body("lastName", equalTo("Smith"))
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCannotGetOtherUserByUsername() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .queryParam("username", User.GREG.username)
                .get("/users/search/findByUsername?projections=withDetails")
                .then()
                .statusCode(403)
    }

    @Test
    @AuthenticatedAsUser(User.ALICE)
    fun userCannotListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(403)
    }

    @Test
    @AuthenticatedAsUser(User.GREG)
    fun adminCanGetUserByUsername() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .queryParam("username", User.ALICE.username)
                .get("/users/search/findByUsername?projections=withDetails")
                .then()
                .statusCode(200)
                .body("username", equalTo("alice"))
    }

    @Test
    @AuthenticatedAsUser(User.GREG)
    fun adminCanListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(200)
                .body("_embedded.users", hasSize<String>(equalTo(3)))
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanListUsers() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users")
                .then()
                .statusCode(200)
                .body("_embedded.users", hasSize<String>(equalTo(3)))
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanCreateUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "username": "alice2",
                    "password": "abc",
                    "email": "qwerty666",
                    "enabled": true,
                    "firstName": "Alice",
                    "lastName": "Smith",
                    "authorities": ["/authorities/ROLE_MANAGER"]
                }""".trimIndent())
                .post("/users?projection=withDetails")
                .then()
                .statusCode(201)
                .body("username", equalTo("alice2"))
                .body("email", equalTo("qwerty666"))
                .body("enabled", `is`(true))
                .body("lastPasswordResetDate", isEmptyOrNullString())
                .body("firstName", equalTo("Alice"))
                .body("lastName", equalTo("Smith"))
                .body("authorities", hasSize<Any>(1))
                .body("authorities[0].name", equalTo("ROLE_MANAGER"))
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanGetAnyUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/users/${User.ALICE.id}")
                .then()
                .statusCode(200)
                .body("username", equalTo("alice"))
    }

    /**
     * this one deletes the user... does not recreate?
     */
    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanDeleteUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .delete("/users/${User.ALICE.id}")
                .then()
                .statusCode(204)
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanUpdateUser() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "username": "alice2",
                    "password": "abcdef",
                    "email": "qwerty666@any.com",
                    "enabled": true
                }""".trimIndent())
                .put("/users/${User.ALICE.id}?projection=withDetails")
                .then()
                .statusCode(200)
                .body("username", equalTo("alice2"))
                .body("email", equalTo("qwerty666@any.com"))
                .body("enabled", `is`(true))
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanUpdateUserLeavingPasswordIntact() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "username": "alice2",
                    "email": "qwerty666@any.com",
                    "enabled": true
                }""".trimIndent())
                .patch("/users/${User.ALICE.id}?projection=withDetails")
                .then()
                .statusCode(200);
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanUpdateAuthorities() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "authorities": ["/authorities/ROLE_MANAGER","/authorities/ROLE_USER"]
                }""".trimIndent())
                .patch("/users/${User.ALICE.id}?projection=withDetails")
                .then()
                .statusCode(200)
                .body("authorities", hasSize<Any>(2))
                .body("authorities.name", containsInAnyOrder("ROLE_MANAGER", "ROLE_USER"))
    }

    @Test
    @AuthenticatedAsUser(User.AGATHA)
    fun managerCanAssignUserRoles() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .contentType("text/uri-list")
                .body("""
                    http://localhost:8080/api/authorities/ROLE_MANAGER
                    """.trimIndent())
                .post("/users/${User.ALICE.id}/authorities")
                .then()
                .statusCode(204)
    }

}