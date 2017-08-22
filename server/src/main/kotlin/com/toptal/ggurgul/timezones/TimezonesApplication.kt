package com.toptal.ggurgul.timezones

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.query.spi.EvaluationContextExtension
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@SpringBootApplication
open class TimezonesApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TimezonesApplication::class.java, *args)
        }
    }

    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun securityExtension(): EvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }

    internal inner class SecurityEvaluationContextExtension : EvaluationContextExtensionSupport() {

        override fun getExtensionId(): String {
            return "security"
        }

        override fun getRootObject(): SecurityExpressionRoot {
            val authentication = SecurityContextHolder.getContext().authentication
            return object : SecurityExpressionRoot(authentication) {}
        }
    }

}
