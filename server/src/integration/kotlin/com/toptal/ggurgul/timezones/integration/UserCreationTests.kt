package com.toptal.ggurgul.timezones.integration

import org.assertj.core.api.Assertions.*
import com.toptal.ggurgul.timezones.TimezonesApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(IntegrationTestContext::class))
@SpringBootTest(classes = arrayOf(TimezonesApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
open class UserCreationTests {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun encodesPassword() {
        assertThat(passwordEncoder.encode("qwerty123")).isNotNull()
    }

}