package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.access.prepost.PreAuthorize

@Api(value = "user", description = "User operations", tags = arrayOf("user"))
@PreAuthorize("hasRole('MANAGER_ROLE') or hasRole('ADMIN_ROLE')")
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User

}
