package com.ggurgul.playground.timezones.integration.tests

import com.ggurgul.playground.timezones.domain.models.security.User
import com.ggurgul.playground.timezones.domain.models.security.UserCode
import com.ggurgul.playground.timezones.domain.models.security.UserCodeType
import com.ggurgul.playground.timezones.domain.services.UserCodeFactory
import com.ggurgul.playground.timezones.integration.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserCodeFactoryTests : AbstractIntegrationTest() {

    @Autowired
    var userCodeFactory: UserCodeFactory? = null

    @Test
    fun fillsUserCodeWithUserDetails() {
        val user = User().apply {
            username = "testusername"
            email = "test@email.com"
        }

        assertThat(userCodeFactory!!.generateFor(user, UserCodeType.REGISTRATION_CONFIRMATION)).isEqualToIgnoringGivenFields(
                UserCode().apply {
                    this.user = user
                    this.type = UserCodeType.REGISTRATION_CONFIRMATION
                    this.sentToEmail = user.email
                }, "code"
        )
    }

}