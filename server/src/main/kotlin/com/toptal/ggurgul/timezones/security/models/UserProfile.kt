package com.toptal.ggurgul.timezones.security.models

import java.io.Serializable

data class UserProfile(
        var firstName: String? = null,
        var lastName: String? = null
) : Serializable