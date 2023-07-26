package edu.miu.eurakdem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurakdemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurakdemApplication.class, args);
	}

}
