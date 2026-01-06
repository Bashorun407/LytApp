package com.bash.LytApp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Lyt microservice REST API Documentation",
				description = "Lyt microservice"
		)
)
public class LytAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LytAppApplication.class, args);
	}

}
