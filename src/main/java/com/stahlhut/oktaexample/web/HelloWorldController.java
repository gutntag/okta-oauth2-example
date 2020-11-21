package com.stahlhut.oktaexample.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/hello")
public class HelloWorldController {

    @GetMapping(value = "/protected")
    public GreetingResource getHelloWorld(@AuthenticationPrincipal Jwt jwt){
        return new GreetingResource("Hello " + jwt.getSubject());
    }

    @GetMapping(value = "/anon")
    public GreetingResource getHelloWorldAnon(){
        return new GreetingResource("Hello World!");
    }


}
