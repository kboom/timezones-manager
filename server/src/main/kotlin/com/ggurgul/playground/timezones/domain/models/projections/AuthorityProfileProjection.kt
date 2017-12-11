package com.ggurgul.playground.timezones.domain.models.projections

import com.ggurgul.playground.timezones.domain.models.security.Authority
import org.springframework.data.rest.core.config.Projection

@Projection(name = "withDetails", types = arrayOf(Authority::class))
interface AuthorityProfileProjection {

    fun getName()

}
