package com.hardik.pottify;

import com.hardik.pottify.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import com.hardik.pottify.controller.MusicController;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
public class PottifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PottifyApplication.class, args);



	}

}
