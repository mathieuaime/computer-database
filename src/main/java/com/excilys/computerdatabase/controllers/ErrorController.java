package com.excilys.computerdatabase.controllers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
    
    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public String NotFoudPage() {
        return "404";
    }
    
    @RequestMapping(value = {"/403"}, method = RequestMethod.GET)
    public String ClientErrorPage() {
        return "403";
    }

    @RequestMapping(value = {"/500"}, method = RequestMethod.GET)
    public String ServorErrorPage() {
        return "500";
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handle(Exception ex) {
        return new ModelAndView("404");
    }

    @ExceptionHandler({ HttpClientErrorException.class, Throwable.class })
    public ModelAndView handleError4xx(HttpServletRequest request, Exception e) {
        LOGGER.debug("403");
        return new ModelAndView("403");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ModelAndView handleError5xx(HttpServletRequest request, Exception e) {
        LOGGER.debug("500");
        return new ModelAndView("500");
    }
    
    @PostConstruct
    public void initApp() {
        LOGGER.debug("Error binding configuration ...");
    }
}
