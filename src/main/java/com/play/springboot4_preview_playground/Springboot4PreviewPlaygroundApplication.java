package com.play.springboot4_preview_playground;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Springboot4PreviewPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot4PreviewPlaygroundApplication.class, args);
	}

   @RequestMapping("/")
	String home() {
		return "Hello World!@" + LocalDateTime.now();
	}

}
