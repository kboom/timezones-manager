package com.ggurgul.playground.timezones.domain.events

import com.ggurgul.playground.timezones.domain.models.security.UserCode
import org.springframework.context.ApplicationEvent

class RegistrationCodeIssuedEvent(userCodes: UserCode) : ApplicationEvent(userCodes) {

    override fun getSource() = super.getSource() as UserCode

}