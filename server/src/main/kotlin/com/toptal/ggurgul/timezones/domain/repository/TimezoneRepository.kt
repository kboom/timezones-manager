package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.projections.TimezoneProjection
import io.swagger.annotations.Api
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize

@Api(value = "timezone", description = "Timezone operations", tags = arrayOf("timezone"))
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RepositoryRestResource(
        excerptProjection = TimezoneProjection::class
)
interface TimezoneRepository : CrudRepository<Timezone, Long> {

}
