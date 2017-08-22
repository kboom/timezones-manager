package com.toptal.ggurgul.timezones.domain.services

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.security.JwtUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
open class UserService
@Autowired
constructor(
        private val userRepository: UserRepository
) {

    fun getActingUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return userRepository.findOne((auth.principal as JwtUser).id)
    }

}