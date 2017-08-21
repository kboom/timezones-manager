package com.toptal.ggurgul.timezones.functional.rules

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*


class AuthenticationRule(
        private val httpClient: HttpClient
) : TestRule {

    var token: String? = null

    override fun apply(base: Statement, description: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            val authenticatedAsOptional = Optional.ofNullable(description.getAnnotation(AuthenticatedAs::class.java))
            if (authenticatedAsOptional.isPresent) {
                val authenticatedAs = authenticatedAsOptional.get()
                val postRequest = HttpPost("http://localhost:8080/auth")
                postRequest.setHeader("Accept", "application/json");
                postRequest.setHeader("Content-type", "application/json");
                postRequest.entity = StringEntity("""{
                        "username": "${authenticatedAs.username}",
                        "password": "${authenticatedAs.password}"
                    }""".trimIndent())
                val response = httpClient.execute(postRequest)
                val responseString = EntityUtils.toString(response.entity)
                token = responseString
                        .replace("{\"token\":\"", "")
                        .replace("\"}", "")
            }
            base.evaluate()
        }
    }

}