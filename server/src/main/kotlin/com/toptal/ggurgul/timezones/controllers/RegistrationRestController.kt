package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.models.security.AuthorityName
import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType
import com.toptal.ggurgul.timezones.domain.repository.UserCodesRepository
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.domain.services.UserCodeFactory
import com.toptal.ggurgul.timezones.exceptions.RegistrationException
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.SystemUser
import com.toptal.ggurgul.timezones.security.models.RegistrationRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional


@RestController
@Api(value = "registration", description = "Registration operations", tags = arrayOf("registration"))
@RequestMapping(value = "/registration")
class RegistrationRestController
@Autowired
constructor(
        private val systemRunner: SystemRunner,
        private val userRepository: UserRepository,
        private val userCodesRepository: UserCodesRepository,
        private val passwordEncoder: BCryptPasswordEncoder,
        private val userCodeFactory: UserCodeFactory
) {

    @ApiOperation(value = "Register account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Registration successful"),
            ApiResponse(code = 500, message = "Registration failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.POST))
//    @Transactional
    @Throws(RegistrationException::class)
    fun createAuthenticationToken(
            @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<*> {

        val user = User()

        user.username = registrationRequest.username
        user.password = passwordEncoder.encode(registrationRequest.password)
        user.email = registrationRequest.email
        user.enabled = false

        systemRunner.runInSystemContext {
            userRepository.save(user)
            val userCode = userCodeFactory.generateFor(user, UserCodeType.REGISTRATION_CONFIRMATION)
            userCodesRepository.save(userCode)
        }

        return ResponseEntity.ok("Registered")
    }

}