package com.toptal.ggurgul.timezones.security.repository

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import com.toptal.ggurgul.timezones.domain.models.security.AuthorityName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository : JpaRepository<Authority, AuthorityName>