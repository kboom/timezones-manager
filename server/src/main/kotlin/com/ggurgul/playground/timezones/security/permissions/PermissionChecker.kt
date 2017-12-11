package com.ggurgul.playground.timezones.security.permissions

interface PermissionChecker {

    fun shouldGrantPermissionFor(grantRequest: PermissionGrantRequest): Boolean

    fun canHandle(grantRequest: PermissionGrantRequest): Boolean

}