package com.nrdChat.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class MainController {

    @RequestMapping("/main")
    public String main() {
        return "Hello World";
    }

    @RequestMapping("/index")
    public String login() {
        return "Hello World from login";
    }
}
