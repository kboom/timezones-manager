package com.ggurgul.playground.timezones.security.permissions

import com.ggurgul.playground.timezones.security.AuthenticatedUser
import java.io.Serializable

data class PermissionGrantRequest(
        val principal: AuthenticatedUser,
        val targetId: Serializable? = null,
        val targetObject: Any? = null,
        val permissionNeeded: Permission
)