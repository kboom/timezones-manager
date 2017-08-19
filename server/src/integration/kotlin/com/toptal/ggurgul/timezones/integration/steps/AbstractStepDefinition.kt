package com.toptal.ggurgul.timezones.integration.steps

import com.toptal.ggurgul.timezones.TimezonesApplication
import com.toptal.ggurgul.timezones.integration.IntegrationTestContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = arrayOf(IntegrationTestContext::class))
@SpringBootTest(classes = arrayOf(TimezonesApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract internal class AbstractStepDefinition