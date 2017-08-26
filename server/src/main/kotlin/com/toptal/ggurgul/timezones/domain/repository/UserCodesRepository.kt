package com.toptal.ggurgul.timezones.domain.repository

import com.toptal.ggurgul.timezones.domain.models.security.UserCode
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.stereotype.Repository

@Repository
@RestResource(exported = false)
interface UserCodesRepository : CrudRepository<UserCode, Long>