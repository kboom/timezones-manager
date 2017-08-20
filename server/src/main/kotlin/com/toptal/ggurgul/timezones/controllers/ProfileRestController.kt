package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.security.JwtTokenUtil
import com.toptal.ggurgul.timezones.security.JwtUser
import com.toptal.ggurgul.timezones.security.models.JwtAuthenticationResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
@Api(value = "profile", description = "Profile operations", tags = arrayOf("profile"))
@RequestMapping("/profile")
class ProfileRestController {

    @Value("\${jwt.header}")
    private val tokenHeader: String? = null

    @Autowired
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @ApiOperation(nickname = "getProfile", value = "Get profile", response = JwtUser::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Got profile"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getAuthenticatedUser(request: HttpServletRequest): JwtUser {
        val token = request.getHeader(tokenHeader)
        val username = jwtTokenUtil!!.getUsernameFromToken(token)
        return userDetailsService!!.loadUserByUsername(username) as JwtUser
    }

}
