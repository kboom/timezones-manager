package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize

@Api(value = "user", description = "User operations", tags = arrayOf("user"))
@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
@RestResource
interface UserRepository : CrudRepository<User, Long> {

}
