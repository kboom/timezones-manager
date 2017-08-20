package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.security.JwtAuthenticationRequest
import com.toptal.ggurgul.timezones.security.JwtTokenUtil
import com.toptal.ggurgul.timezones.security.JwtUser
import com.toptal.ggurgul.timezones.security.service.JwtAuthenticationResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@Api(value = "auth", description = "Authentication operations")
@RequestMapping(value = "/auth")
class AuthenticationRestController
@Autowired
constructor(
        @Value("\${jwt.header}")
        private val tokenHeader: String,
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenUtil: JwtTokenUtil,
        private val userDetailsService: UserDetailsService
) {

    @ApiOperation(value = "Authenticate", response = JwtAuthenticationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully authenticated"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(
            @RequestBody authenticationRequest: JwtAuthenticationRequest, device: Device): ResponseEntity<*> {

        val authentication = authenticate(authenticationRequest)

        SecurityContextHolder.getContext().authentication = authentication

        if (authenticationRequest.username != "admin") {
            val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
            val token = jwtTokenUtil.generateToken(userDetails, device)
            return ResponseEntity.ok(JwtAuthenticationResponse(token))
        } else {
            val token = jwtTokenUtil.generateAdminToken(device)
            return ResponseEntity.ok(JwtAuthenticationResponse(token))
        }
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): ResponseEntity<*> {
        val token = request.getHeader(tokenHeader)
        val username = jwtTokenUtil.getUsernameFromToken(token)
        val user = userDetailsService.loadUserByUsername(username) as JwtUser

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.lastPasswordResetDate)!!) {
            val refreshedToken = jwtTokenUtil.refreshToken(token)
            return ResponseEntity.ok(JwtAuthenticationResponse(refreshedToken))
        } else {
            return ResponseEntity.badRequest().body<Any>(null)
        }
    }

    private fun authenticate(authenticationRequest: JwtAuthenticationRequest): Authentication {
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.username,
                        authenticationRequest.password
                )
        )
    }

}
