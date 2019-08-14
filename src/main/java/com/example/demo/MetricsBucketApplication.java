package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import dev.dickinson.controllers.BucketController;

@SpringBootApplication
@ComponentScan(basePackageClasses = BucketController.class)
public class MetricsBucketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsBucketApplication.class, args);
	}

}
