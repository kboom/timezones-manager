package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import io.swagger.annotations.Api
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize

@Api(value = "timezone", description = "Timezone operations", tags = arrayOf("timezone"))
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RepositoryRestResource(
//        excerptProjection = TimezoneProjection::class
)
interface TimezoneRepository : PagingAndSortingRepository<Timezone, Long> {

}
