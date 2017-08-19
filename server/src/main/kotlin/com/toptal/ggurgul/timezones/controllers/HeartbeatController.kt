package com.toptal.ggurgul.timezones.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartbeatController {

    @RequestMapping("/heartbeat")
    fun index(): String {
        return "ping"
    }

}