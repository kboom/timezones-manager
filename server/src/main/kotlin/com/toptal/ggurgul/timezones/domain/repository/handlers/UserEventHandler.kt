package com.toptal.ggurgul.timezones.domain.repository.handlers

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.security.repository.AuthorityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
@RepositoryEventHandler(User::class)
open class UserEventHandler
@Autowired constructor(
        private val passwordEncoder: BCryptPasswordEncoder,
        private val userRepository: UserRepository,
        private val authorityRepository: AuthorityRepository
) {

    @HandleBeforeCreate
    fun handleUserCreate(user: User) {
        user.password = passwordEncoder.encode(user.password)
        user.authorities = authorityRepository.findAll(user.authorities.map { it.name })
    }

    @HandleBeforeSave
    fun handleUserUpdate(user: User) {
        if (user.password == null || user.password == "") {
            val storedUser = userRepository.findOne(user.id)
            user.password = storedUser.password
        } else {
            user.password = passwordEncoder.encode(user.password)
        }
        user.authorities = authorityRepository.findAll(user.authorities.map { it.name })
    }

    @HandleBeforeLinkSave
    fun handleBeforeLinkSave(authorities: MutableList<Authority>) {
        System.out.println("abc")
    }

}