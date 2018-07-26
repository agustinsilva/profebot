package ar.com.profebot.ia.ar.com.profebot.ia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class Controller {

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "Hello, World!";
    }
}
