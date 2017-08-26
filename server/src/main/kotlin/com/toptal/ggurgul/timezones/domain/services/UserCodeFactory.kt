package com.toptal.ggurgul.timezones.domain.services

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.models.security.UserCode
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.SecureRandom


@Component
open class UserCodeFactory {

    fun generateFor(user: User, codeType: UserCodeType): UserCode {
        val userCode = UserCode()
        userCode.user = user
        userCode.type = codeType
        userCode.sentToEmail = user.email
        userCode.code = generateRandomHexToken(16)
        return userCode
    }

    private fun generateRandomHexToken(byteLength: Int): String {
        val secureRandom = SecureRandom()
        val token = ByteArray(byteLength)
        secureRandom.nextBytes(token)
        return BigInteger(1, token).toString(16)
    }


}