package com.ggurgul.playground.timezones.functional.rules

import com.ggurgul.playground.timezones.functional.database.User


@Target(AnnotationTarget.FUNCTION)
annotation class AuthenticatedAsUser(
        val user: User
)