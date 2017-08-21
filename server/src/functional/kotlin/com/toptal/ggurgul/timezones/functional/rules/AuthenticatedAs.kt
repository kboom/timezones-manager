package com.toptal.ggurgul.timezones.functional.rules

@Target(AnnotationTarget.FUNCTION)
annotation class AuthenticatedAs(
        val username: String,
        val password: String
)