package com.nov.spring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {



    @Autowired
    public MainController() {
    }

    @GetMapping
    public String getPeople() {
        return "main/main";
    }


}