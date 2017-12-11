package com.ggurgul.playground.timezones.integration.tests

import com.ggurgul.playground.timezones.domain.services.UserService
import com.ggurgul.playground.timezones.integration.AbstractIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTests : AbstractIntegrationTest() {

    @Autowired
    var userService: UserService? = null

    @Test
    fun canCreateUser() {

    }

}