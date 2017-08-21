package com.toptal.ggurgul.timezones.security.models

import java.io.Serializable

data class JwtAuthenticationRequest(
        var username: String? = null,
        var password: String? = null
) : Serializable