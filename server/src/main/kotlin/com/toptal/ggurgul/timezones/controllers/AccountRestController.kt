package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.exceptions.WrongConfirmationCodeException
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.models.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "account", description = "Account operations", tags = arrayOf("account"))
@RequestMapping("/account")
class AccountRestController
@Autowired constructor(
        private val userService: UserService,
        private val systemRunner: SystemRunner
) {

    @ApiOperation(value = "Get profile", response = UserAccount::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Got account"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getProfile(): ResponseEntity<UserAccount> {
        return ResponseEntity.ok(UserAccount.fromUser(userService.getActingUser()));
    }

    @ApiOperation(value = "Update account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Got account"),
            ApiResponse(code = 401, message = "Authentication failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    fun putProfile(@RequestBody userAccount: UserAccount): ResponseEntity<Any> {
        return try {
            userService.updateProfile(userAccount)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.status(403).build()
        }
    }

    @ApiOperation(value = "Change password")
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

    @ApiOperation(value = "Reset password")
    @ApiResponses(
            ApiResponse(code = 200, message = "Changed password"),
            ApiResponse(code = 401, message = "Wrong password")
    )
    @RequestMapping(value = "/password/reset", method = arrayOf(RequestMethod.POST))
    @Throws(AuthenticationException::class)
    fun resetPassword(
            @RequestBody passwordResetRequest: PasswordResetRequest): ResponseEntity<Any> {
        return try {
            userService.resetPassword(passwordResetRequest.email!!)
            ResponseEntity.ok().build();
        } catch (e: Exception) {
            ResponseEntity.status(200).build() // always 200 not to give out email hints
        }
    }

    @ApiOperation(value = "Set new password")
    @ApiResponses(
            ApiResponse(code = 200, message = "Changed password"),
            ApiResponse(code = 400, message = "Wrong confirmation code")
    )
    @RequestMapping(value = "/password/reset/confirmation", method = arrayOf(RequestMethod.POST))
    fun confirmPasswordReset(
            @RequestBody passwordResetRequest: SetNewPasswordRequest): ResponseEntity<Any> {
        return try {
            systemRunner.runInSystemContext {
                userService.confirmResetPassword(passwordResetRequest.code!!, passwordResetRequest.newPassword!!)
            }
            return ResponseEntity.ok("Changed")
        } catch (e: WrongConfirmationCodeException) {
            ResponseEntity.status(400).body("Wrong confirmation code")
        }
    }

}
