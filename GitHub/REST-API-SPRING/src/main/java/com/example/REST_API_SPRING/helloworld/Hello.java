package com.example.REST_API_SPRING.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    //hello
    //@RequestMapping(method = RequestMethod.GET,path = "/hello")
    @GetMapping(path = "/hello")//doesn't require a method
    public String sayHello() {
        return "Hello World";
    }
    @GetMapping(path = "/hello-bean")//doesn't require a method
    public HelloBean HelloBean() {
        return new HelloBean("Hello-Bean");
    }
    //Path parameters
    @GetMapping(path = "/hello-path/path-var/{name}")//doesn't require a method
    public HelloBean HelloPathVar(@PathVariable("name") String name) {
        return new HelloBean("Hello "+name);
    }
}
