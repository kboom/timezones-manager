package com.toptal.ggurgul.timezones.security.service

import com.toptal.ggurgul.timezones.security.JwtUser
import com.toptal.ggurgul.timezones.security.JwtUserFactory
import com.toptal.ggurgul.timezones.security.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsServiceImpl
@Autowired constructor(
        private val userDetailsRepository: UserDetailsRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userDetailsRepository.findByUsername(username)

        return user.map<JwtUser>(JwtUserFactory::create)
                .orElseThrow {
                    UsernameNotFoundException(
                            String.format("No user found with username '%s'.", username))
                }
    }

}
