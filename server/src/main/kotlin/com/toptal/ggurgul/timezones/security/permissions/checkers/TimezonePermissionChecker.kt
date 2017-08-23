package com.toptal.ggurgul.timezones.security.permissions.checkers

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.repository.TimezoneRepository
import com.toptal.ggurgul.timezones.security.permissions.Permission.*
import com.toptal.ggurgul.timezones.security.permissions.PermissionChecker
import com.toptal.ggurgul.timezones.security.permissions.PermissionGrantRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class TimezonePermissionChecker
@Autowired constructor(
        private val timezoneRepository: TimezoneRepository
) : PermissionChecker {

    companion object {
        val handledPermissions = setOf(READ_TIMEZONE, EDIT_TIMEZONE, DELETE_TIMEZONE)
    }

    override fun shouldGrantPermissionFor(grantRequest: PermissionGrantRequest): Boolean {
        val timezone = timezoneRepository.findById(getIdFrom(grantRequest))
        return timezone.owner!!.id == grantRequest.principal.id
    }

    override fun canHandle(grantRequest: PermissionGrantRequest) =
            handledPermissions.contains(grantRequest.permissionNeeded)

    private fun getIdFrom(grantRequest: PermissionGrantRequest) =
            if (grantRequest.targetId != null) grantRequest.targetId as Long
            else (grantRequest.targetObject!! as Timezone).id!!

}