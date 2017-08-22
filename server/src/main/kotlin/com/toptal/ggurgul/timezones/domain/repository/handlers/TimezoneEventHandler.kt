package com.toptal.ggurgul.timezones.domain.repository.handlers

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.repository.TimezoneRepository
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.security.JwtUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import org.springframework.security.core.context.SecurityContextHolder



@Component
@RepositoryEventHandler(Timezone::class)
open class TimezoneEventHandler
@Autowired constructor(
        val userRepository: UserRepository,
        val timezoneRepository: TimezoneRepository
) {

    @HandleBeforeCreate
    fun handleTimezoneSave(timezone: Timezone) {
        System.out.println("abc")
    }

    @HandleBeforeSave
    fun handleTimezoneUpdate(timezone: Timezone) {
        val existingTimezone = timezoneRepository.findOne(timezone.id)
        timezone.owner = existingTimezone.owner
    }

}