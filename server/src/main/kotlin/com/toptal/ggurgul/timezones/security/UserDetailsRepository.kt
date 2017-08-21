package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.models.security.User
import org.springframework.data.repository.Repository
import org.springframework.data.rest.core.annotation.RestResource
import java.util.*

@RestResource(exported = false)
interface UserDetailsRepository : Repository<User, String> {

    fun findByUsername(username: String): Optional<User>

}