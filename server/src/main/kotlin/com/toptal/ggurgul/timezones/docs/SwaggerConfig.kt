package com.toptal.ggurgul.timezones.docs

import com.google.common.base.Predicates.not
import com.google.common.base.Predicates.or
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Profile("docs")
@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration::class, BeanValidatorPluginsConfiguration::class)
open class SwaggerConfig {

    @Bean
    open fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(or(
                        regex("/user*"),
                        regex("/auth*"),
                        regex("/timezones*")
                ))
                .build()
                .securitySchemes(arrayListOf(apiKey()))
                .securityContexts(arrayListOf(securityContext()))
    }

    @Bean
    open fun swaggerSecurity(): SecurityConfiguration {
        return SecurityConfiguration(
                null,
                null,
                null,
                null,
                "",
                ApiKeyVehicle.HEADER,
                "Authorization",
                ",")
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Bearer", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(not(PathSelectors.regex("/auth*")))
                .build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return arrayListOf(SecurityReference("mykey", authorizationScopes))
    }

}