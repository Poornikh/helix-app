package com.helix.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.helix.app"})
@EnableTransactionManagement
public class HelixAppApplication {

	public static void main(String[] args) {
		
		/*SpringApplicationBuilder builder = new SpringApplicationBuilder(HelixAppApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);*/
		SpringApplication.run(HelixAppApplication.class, args);
	}
	

}
