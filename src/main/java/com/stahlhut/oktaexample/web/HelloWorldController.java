package com.stahlhut.oktaexample.web;

import com.stahlhut.oktaexample.services.AuthorizationService;
import com.stahlhut.oktaexample.web.resources.GreetingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/hello")
public class HelloWorldController {

    @Autowired
    AuthorizationService authorizationService;

    @GetMapping(value = "/protected")
    @PreAuthorize("@authorizationServiceImplOktaJwt.authorize('ROLE_ADMIN')") //todo: use autowired interface
    public GreetingResource getProtectedList(){
        return new GreetingResource("Hello ");
    }

    @GetMapping(value = "/protected/{resourceId}")
    @PreAuthorize("@authorizationServiceImplOktaJwt.authorize('ROLE_CUSTOMER', 'protectedresource', #resourceId)") //todo: use autowired interface
    public GreetingResource getProtectedResource(@PathVariable String resourceId){
        return new GreetingResource("This is resource " + resourceId);
    }


    @GetMapping(value = "/anon")
    public GreetingResource getHelloWorldAnon(){
        return new GreetingResource("Hello World!");
    }


}
