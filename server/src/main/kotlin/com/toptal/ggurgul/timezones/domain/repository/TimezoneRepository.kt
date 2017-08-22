package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import io.swagger.annotations.Api
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Repository

@Api(value = "timezone", description = "Timezone operations", tags = arrayOf("timezone"))
@PreAuthorize("isAuthenticated()")
@RestResource
@Repository
interface TimezoneRepository : CrudRepository<Timezone, Long> {

    @Query("select t from Timezone t where t.owner.username like ?#{hasRole('ROLE_ADMIN') ? '%' : principal.username}")
    override fun findAll(): MutableIterable<Timezone>

}
