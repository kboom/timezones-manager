package com.toptal.ggurgul.timezones.projections

import com.toptal.ggurgul.timezones.domain.models.Timezone
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.core.config.Projection

@Projection(name = "timezone", types = arrayOf(Timezone::class))
interface TimezoneProjection {

    val name: String

    val locationName: String

    @get:Value("#{target.owner.username}")
    val ownerName: String

}