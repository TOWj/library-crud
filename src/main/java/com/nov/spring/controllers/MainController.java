package com.nov.spring.controllers;


import com.nov.spring.dao.BookDAO;
import com.nov.spring.dao.PersonDAO;
import com.nov.spring.models.Book;
import com.nov.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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