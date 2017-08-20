package com.toptal.ggurgul.timezones.integration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


@Configuration
open class IntegrationTestContext {

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.url = "jdbc:h2:mem:db;MODE=PostgreSQL"
        dataSource.username = "sa"
        dataSource.password = ""

        return dataSource
    }

}