package com.ggurgul.playground.timezones.security.service

import com.ggurgul.playground.timezones.domain.repository.UserRepository
import com.ggurgul.playground.timezones.security.JwtUser
import com.ggurgul.playground.timezones.security.JwtUserFactory
import com.ggurgul.playground.timezones.security.SystemRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsServiceImpl
@Autowired constructor(
        private val userRepository: UserRepository,
        private val systemRunner: SystemRunner
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = systemRunner.runInSystemContext {
            userRepository.findByUsername(username)
        }

        return user.map<JwtUser>(JwtUserFactory::create)
                .orElseThrow {
                    UsernameNotFoundException(
                            String.format("No user found with username '%s'.", username))
                }
    }

}
