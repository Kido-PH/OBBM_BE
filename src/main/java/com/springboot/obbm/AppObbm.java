package com.springboot.obbm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.springboot.obbm")
public class AppObbm {

	public static void main(String[] args) {
		SpringApplication.run(AppObbm.class, args);
	}

}
