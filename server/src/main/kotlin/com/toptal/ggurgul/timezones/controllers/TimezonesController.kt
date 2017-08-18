package com.toptal.ggurgul.timezones.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TimezonesController {

    @RequestMapping("/hello")
    fun index(): String {
        return "hello world"
    }

}