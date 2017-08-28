package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.security.JwtTokenUtil
import com.toptal.ggurgul.timezones.security.JwtUser
import com.toptal.ggurgul.timezones.security.models.JwtAuthenticationResponse
import com.toptal.ggurgul.timezones.security.models.PasswordChangeRequest
import com.toptal.ggurgul.timezones.security.models.UserProfile
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
@Api(value = "profile", description = "Profile operations", tags = arrayOf("profile"))
@RequestMapping("/profile")
class ProfileRestController
@Autowired constructor(
        private val userService: UserService
) {

    @ApiOperation(value = "Get profile", response = JwtUser::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Got profile"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getProfile(): ResponseEntity<UserProfile> {
        return ResponseEntity.ok(UserProfile.fromUser(userService.getActingUser()));
    }

    @ApiOperation(value = "Update profile")
    @ApiResponses(
            ApiResponse(code = 200, message = "Got profile"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    fun putProfile(@RequestBody userProfile: UserProfile): ResponseEntity<Any> {
        return try {
            userService.updateProfile(userProfile)
            ResponseEntity.ok().build()
        } catch(e: Exception) {
            ResponseEntity.status(403).build()
        }
    }

    @ApiOperation(value = "Change password", response = JwtAuthenticationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Changed password"),
            ApiResponse(code = 401, message = "Wrong password")
    )
    @RequestMapping(value = "/password", method = arrayOf(RequestMethod.POST))
    @Throws(AuthenticationException::class)
    fun changePassword(
            @RequestBody passwordChangeRequest: PasswordChangeRequest): ResponseEntity<Any> {
        val actingUser = userService.getActingUser();
        return try {
            userService.changePasswordFor(actingUser,
                    passwordChangeRequest.oldPassword!!, passwordChangeRequest.newPassword!!)
            ResponseEntity.ok().build();
        } catch (e: Exception) {
            ResponseEntity.status(403).build()
        }
    }

}
