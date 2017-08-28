package com.toptal.ggurgul.timezones.security.models

import java.io.Serializable

data class PasswordChangeRequest(
        var oldPassword: String? = null,
        var newPassword: String? = null
) : Serializable