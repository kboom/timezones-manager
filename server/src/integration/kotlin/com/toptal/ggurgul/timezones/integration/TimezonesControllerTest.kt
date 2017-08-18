package com.toptal.ggurgul.timezones.integration

import com.toptal.ggurgul.timezones.TimezonesApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(TimezonesApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimezonesControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun whenCalled_shouldReturnHello() {
        val result = testRestTemplate
                .getForEntity("/hello", String::class.java)

        assertThat(result?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result?.body).isEqualTo("hello world")
    }

}