package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.Api
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize
import java.util.*

@Primary
@Api(value = "user", description = "User operations", tags = arrayOf("user"))
//@PreAuthorize("hasRole('ROLE_SYSTEM')") // todo fix - something is not secured as this fails!
@RepositoryRestResource
interface UserRepository : CrudRepository<User, Long> {

    /**
     * Internal method used by security
     */
    @RestResource(exported = false)
    fun findByUsername(username: String): Optional<User>

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_MANAGER', 'ROLE_ADMIN') or #userId == principal.id")
    override fun findOne(@Param("userId") id: Long): User

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_MANAGER', 'ROLE_ADMIN') or #user.id == principal.id")
    override fun delete(@Param("user") user: User?)

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_MANAGER', 'ROLE_ADMIN') or  #user.id == principal.id")
    override fun <S : User?> save(@Param("user") user: S): S

    @PreAuthorize("hasAnyRole('ROLE_SYSTEM', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    override fun findAll(): MutableIterable<User>

}
