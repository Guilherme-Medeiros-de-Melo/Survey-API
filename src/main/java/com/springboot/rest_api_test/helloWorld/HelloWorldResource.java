package com.springboot.rest_api_test.helloWorld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class HelloWorldResource {
	
	@RequestMapping("/hello-world")
	//@ResponseBody
	public String hewlloWorld() {
		return "Hello World";
	}
	
	@RequestMapping("/hello-world-bean")
	//@ResponseBody
	public HelloWorldBean hewlloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	@RequestMapping("/hello-world-path-param/{name}/{message}")
	//@ResponseBody
	public HelloWorldBean hewlloWorldPathParam(
			@PathVariable String name,
			@PathVariable String message) {
		return new HelloWorldBean("Hello World, " + name + ", " + message);
	}
}
