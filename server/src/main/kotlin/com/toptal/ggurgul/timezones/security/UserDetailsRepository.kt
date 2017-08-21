package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.models.security.User
import org.springframework.data.repository.Repository
import java.util.*

interface UserDetailsRepository : Repository<User, String> {

    fun findByUsername(username: String): Optional<User>

}