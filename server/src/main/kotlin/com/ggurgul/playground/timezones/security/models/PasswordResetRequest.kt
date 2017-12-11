package com.ggurgul.playground.timezones.security.models

import java.io.Serializable

data class PasswordResetRequest(
        var email: String? = null
) : Serializable