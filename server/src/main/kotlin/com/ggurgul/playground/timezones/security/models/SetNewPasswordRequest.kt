package com.ggurgul.playground.timezones.security.models

import java.io.Serializable

data class SetNewPasswordRequest(
        var newPassword: String? = null,
        var code: String? = null
) : Serializable