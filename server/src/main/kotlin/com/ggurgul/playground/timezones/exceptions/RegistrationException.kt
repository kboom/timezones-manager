package com.ggurgul.playground.timezones.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Error during registration")
class RegistrationException : RuntimeException()