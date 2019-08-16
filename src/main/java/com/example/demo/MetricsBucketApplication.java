package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import dev.dickinson.services.AmazonClient;

@SpringBootApplication
@ComponentScan("dev.dickinson")
public class MetricsBucketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsBucketApplication.class, args);
	}

}
