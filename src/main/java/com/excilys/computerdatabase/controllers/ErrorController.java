package com.excilys.computerdatabase.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleError404(HttpServletRequest request, Exception e) {
        return new ModelAndView("404");
    }

    @ExceptionHandler({ HttpClientErrorException.class, Throwable.class })
    @ResponseStatus
    public ModelAndView handleError4xx(HttpServletRequest request, Exception e) {
        return new ModelAndView("403");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseStatus
    public ModelAndView handleError5xx(HttpServletRequest request, Exception e) {
        return new ModelAndView("500");
    }
}
