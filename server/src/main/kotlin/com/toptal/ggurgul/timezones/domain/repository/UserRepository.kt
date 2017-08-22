package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Repository

@Primary
@Api(value = "user", description = "User operations", tags = arrayOf("user"))
@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
@RestResource
@Repository
interface UserRepository : CrudRepository<User, Long> {

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN') or #userId == principal.id")
    override fun findOne(@Param("userId") id: Long): User

}
