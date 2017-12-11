package com.ggurgul.playground.timezones.domain.events

import com.ggurgul.playground.timezones.domain.models.security.UserCode
import org.springframework.context.ApplicationEvent

class PasswordResetCodeIssued(userCode: UserCode) : ApplicationEvent(userCode) {

    override fun getSource() = super.getSource() as UserCode

}