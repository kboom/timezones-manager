package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import io.swagger.annotations.Api
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize

@Api(value = "timezone", description = "Timezone operations", tags = arrayOf("timezone"))
@PreAuthorize("isAuthenticated()")
@RepositoryRestResource
interface TimezoneRepository : CrudRepository<Timezone, Long> {

    @Query("select t from Timezone t where t.owner.username like ?#{hasRole('ROLE_ADMIN') ? '%' : principal.username}")
    override fun findAll(): MutableIterable<Timezone>

    @PreAuthorize("hasRole('ROLE_ADMIN') or #timezone.owner.id == principal.id")
    override fun findAll(@Param("timezoneIds") timezoneIds: MutableIterable<Long>?): MutableIterable<Timezone>

    @PreAuthorize("#timezone.id == null or hasRole('ROLE_ADMIN') or #timezone.owner.id == principal.id")
    override fun <S : Timezone?> save(@Param("timezone") p0: S): S

    @PreAuthorize("hasRole('ROLE_ADMIN') or #timezone.owner.id == principal.id")
    override fun delete(@Param("timezone") timezone: Timezone)

}
