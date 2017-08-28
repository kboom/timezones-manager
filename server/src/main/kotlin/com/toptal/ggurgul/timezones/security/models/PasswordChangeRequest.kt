package com.toptal.ggurgul.timezones.security.models

data class PasswordChangeRequest(
        var oldPassword: String? = null,
        var newPassword: String? = null
) {
}