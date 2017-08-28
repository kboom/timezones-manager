package com.toptal.ggurgul.timezones.security.models

import com.toptal.ggurgul.timezones.domain.models.security.User
import java.io.Serializable

data class UserProfile(
        var username: String? = null,
        var email: String? = null,
        var firstName: String? = null,
        var lastName: String? = null
) : Serializable {

    companion object {

        fun fromUser(user: User): UserProfile {
            return UserProfile(
                    username = user.username,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName
            )
        }

    }

}