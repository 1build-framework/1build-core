package dev.onebuild.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.core.domain.model.config.ScriptParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {

	@Autowired
	private ScriptParameters scriptParameters;

	public static void main(String[] args) {
		SpringApplication.run(
				new Class<?>[] { Application.class}, args);
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ObjectMapper mapper = new ObjectMapper();
    try {
      scriptParameters.addParameter("databaseTypes",
          mapper.writeValueAsString(
							List.of(
									Map.of("id", "PG", "value", "PostgreSQL"),
									Map.of("id", "MySQL", "value", "My SQL"))
          ));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}