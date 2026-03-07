package com.hoosiercoder.dispatchtool.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: HoosierCoder
 *
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // This returns the name of the html file in src/main/resources/templates
        return "login";
    }

    @GetMapping("/")
    public String index() {
        // Once logged in, redirect to a dashboard or home page
        return "index";
    }
}
