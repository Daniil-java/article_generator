package com.education.articlegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableCaching
public class ArticleGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ArticleGeneratorApplication.class, args);
	}

}
