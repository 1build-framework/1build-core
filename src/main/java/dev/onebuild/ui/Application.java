package dev.onebuild.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.ui.config.DomainConfiguration;
import dev.onebuild.ui.config.TemplateConfiguration;
import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
				new Class<?>[] {OneBuildConfigs.class, TemplateConfiguration.class, DomainConfiguration.class, Application.class}, args);
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ObjectMapper mapper = new ObjectMapper();

    try {
      scriptParameters.addParameter("databaseTypes",
          mapper.writeValueAsString(
							List.of(IdValue.builder().id("PG").value("PostgreSQL").build(),
									IdValue.builder().id("MySQL").value("My SQL").build())
          ));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}