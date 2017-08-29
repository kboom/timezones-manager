package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.exceptions.WrongConfirmationCodeException
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.models.PasswordChangeRequest
import com.toptal.ggurgul.timezones.security.models.PasswordResetRequest
import com.toptal.ggurgul.timezones.security.models.SetNewPasswordRequest
import com.toptal.ggurgul.timezones.security.models.UserAccount
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(value = "account", description = "Account operations", tags = arrayOf("account"))
@RequestMapping("/account")
class AccountRestController
@Autowired constructor(
        private val userService: UserService,
        private val systemRunner: SystemRunner
) {

    @ApiOperation(value = "Get profile", response = UserAccount::class)
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getProfile(): ResponseEntity<UserAccount> {
        return ResponseEntity.ok(UserAccount.fromUser(userService.getActingUser()));
    }

    @ApiOperation(value = "Update account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    fun putProfile(@RequestBody @Valid userAccount: UserAccount) {
        userService.updateProfile(userAccount)
    }

    @ApiOperation(value = "Change password")
    @RequestMapping(value = "/password", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Throws(AuthenticationException::class)
    fun changePassword(
            @RequestBody passwordChangeRequest: PasswordChangeRequest) {
        val actingUser = userService.getActingUser();
        userService.changePasswordFor(actingUser,
                passwordChangeRequest.oldPassword!!, passwordChangeRequest.newPassword!!)
    }

    @ApiOperation(value = "Reset password")
    @RequestMapping(value = "/password/reset", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Throws(AuthenticationException::class)
    fun resetPassword(
            @RequestBody passwordResetRequest: PasswordResetRequest) {
        userService.resetPassword(passwordResetRequest.email!!)
    }

    @ApiOperation(value = "Set new password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/password/reset/confirmation", method = arrayOf(RequestMethod.POST))
    fun confirmPasswordReset(
            @RequestBody passwordResetRequest: SetNewPasswordRequest) {
        systemRunner.runInSystemContext {
            userService.confirmResetPassword(passwordResetRequest.code!!, passwordResetRequest.newPassword!!)
        }
    }

}
