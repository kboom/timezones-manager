package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.exceptions.RegistrationException
import com.toptal.ggurgul.timezones.security.models.RegistrationRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "registration", description = "Registration operations", tags = arrayOf("registration"))
@RequestMapping(value = "/registration")
class RegistrationRestController {

    @ApiOperation(value = "Register account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Registration successful"),
            ApiResponse(code = 500, message = "Registration failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @Throws(RegistrationException::class)
    fun createAuthenticationToken(
            @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<*> {
        return ResponseEntity.ok("Registered")
    }

}