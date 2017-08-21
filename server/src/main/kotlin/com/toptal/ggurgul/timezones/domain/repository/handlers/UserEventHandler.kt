package com.toptal.ggurgul.timezones.domain.repository.handlers

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
@RepositoryEventHandler(User::class)
open class UserEventHandler
@Autowired constructor(
        val passwordEncoder: BCryptPasswordEncoder,
        val userRepository: UserRepository
) {

    @HandleBeforeCreate
    fun handleUserCreate(user: User) {
        user.password = passwordEncoder.encode(user.getPassword())
    }

    @HandleBeforeSave
    fun handleUserUpdate(user: User) {
        if (user.password == null || user.password == "") {
            val storedUser = userRepository.findOne(user.id)
            user.password = storedUser.password
        } else {
            user.password = passwordEncoder.encode(user.password)
        }
    }

}