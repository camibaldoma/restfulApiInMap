package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.entity.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    //Se debe indicar que esto va a ser un endpoint
    @GetMapping("/greeting")
    public Greeting helloWorl(@RequestParam(value="name",defaultValue = "World") String name){
        return new Greeting(counter.incrementAndGet(),String.format(template,name));
    }
}
