package com.toptal.ggurgul.timezones.exceptions.advice;

import com.toptal.ggurgul.timezones.exceptions.ApplicationErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseBody
    ResponseEntity<ApplicationErrorMessage> handle(Exception exception) throws Exception {
        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if(annotation == null) {
            throw exception;
        }
        HttpStatus responseStatus = annotation.code();
        ApplicationErrorMessage body = new ApplicationErrorMessage(responseStatus.value(), annotation.reason());
        return new ResponseEntity<ApplicationErrorMessage>(body, responseStatus);
    }

}