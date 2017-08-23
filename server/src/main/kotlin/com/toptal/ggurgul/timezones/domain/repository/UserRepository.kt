package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize

@Primary
@Api(value = "user", description = "User operations", tags = arrayOf("user"))
@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
@RepositoryRestResource
interface UserRepository : CrudRepository<User, Long> {

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN') or #userId == principal.id")
    override fun findOne(@Param("userId") id: Long): User

}
