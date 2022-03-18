package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"service", "controller", "config"})
@EntityScan("model")
@EnableJpaRepositories("repository")
public class AssignmentTwo2022Application {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentTwo2022Application.class, args);
	}

}
