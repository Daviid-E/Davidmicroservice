package com.daviidweb.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daviidweb.model.HelloWorldBean;

@RestController
public class HelloWorldController {
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/hello-world")
	public HelloWorldBean helloworldbean() {
		return new HelloWorldBean("Hello World");
	}
	@GetMapping("/hello-world-internationalized")
	public String helloworldbeanInternationalized(//@RequestHeader(name="Accept-Language", required=false)Locale locale
			) {
		return messageSource.getMessage("good.morning.message", null,"Default message", //locale
				LocaleContextHolder.getLocale());
	}
	@GetMapping("/hello-world/path-variable/{name}")
	public HelloWorldBean helloworldpathvariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s", name));
	}
	@GetMapping("/hello-world/addtext")
	public HelloWorldBean addtext(@RequestParam String name) {
		return new HelloWorldBean(name);
	}
	
}
