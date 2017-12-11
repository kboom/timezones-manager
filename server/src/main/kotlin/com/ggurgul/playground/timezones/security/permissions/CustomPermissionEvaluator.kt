package com.ggurgul.playground.timezones.security.permissions

import com.ggurgul.playground.timezones.security.AuthenticatedUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import com.ggurgul.playground.timezones.security.permissions.Permission.Companion.permissionOf

@Component
class CustomPermissionEvaluator
@Autowired
constructor(
        private val permissionChecker: PermissionChecker
) : PermissionEvaluator {

    override fun hasPermission(authentication: Authentication, targetDomainObject: Any?, permissionText: Any): Boolean {
        return checkPermission(PermissionGrantRequest(
                principal = authentication.principal as AuthenticatedUser,
                targetObject = targetDomainObject,
                permissionNeeded = permissionOf(permissionText.toString())
        ))
    }

    override fun hasPermission(authentication: Authentication, targetId: Serializable?, targetType: String, permissionText: Any): Boolean {
        return checkPermission(PermissionGrantRequest(
                principal = authentication.principal as AuthenticatedUser,
                targetId = targetId,
                permissionNeeded = permissionOf(permissionText.toString())
        ))
    }

    private fun checkPermission(permissionGrantRequest: PermissionGrantRequest): Boolean {
        return if (permissionChecker.canHandle(permissionGrantRequest))
            permissionChecker.shouldGrantPermissionFor(permissionGrantRequest)
        else false
    }

}