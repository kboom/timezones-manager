package com.toptal.ggurgul.timezones

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TimezonesApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TimezonesApplication::class.java, *args)
        }
    }

}
