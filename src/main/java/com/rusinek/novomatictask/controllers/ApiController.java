package com.rusinek.novomatictask.controllers;

import com.rusinek.novomatictask.exceptions.ResourceNotFoundException;
import com.rusinek.novomatictask.model.Statistics;
import com.rusinek.novomatictask.services.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Adrian Rusinek on 24.04.2019
 **/
@RestController
@Slf4j
public class ApiController {

    private final DataService dataService;

    public ApiController(DataService dataService) {
        this.dataService = dataService;
    }


    @RequestMapping("/statistics/{user}")
    public Statistics getInformations(@PathVariable String user) {
        return dataService.authenticate(user);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound() {

        log.error("Handling not found exception");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");

        return modelAndView;
    }

}