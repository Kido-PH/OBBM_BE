package com.springboot.obbm;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import vn.payos.PayOS;

@SpringBootApplication
@ComponentScan(basePackages = "com.springboot.obbm")
@EnableFeignClients
public class AppObbm {
	public static void main(String[] args) {
		SpringApplication.run(AppObbm.class, args);
	}
}
