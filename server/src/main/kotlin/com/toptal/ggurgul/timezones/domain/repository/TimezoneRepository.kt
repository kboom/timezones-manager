package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.Timezone
import com.toptal.ggurgul.timezones.domain.models.security.User
import org.springframework.data.jpa.repository.JpaRepository

interface TimezoneRepository : JpaRepository<Timezone, Long> {

    fun findByOwner(owner: User): Timezone

}
