package dev.onebuild.ui;

import dev.onebuild.ui.config.model.CssConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	@Autowired
	private CssConfigs cssConfigs;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
