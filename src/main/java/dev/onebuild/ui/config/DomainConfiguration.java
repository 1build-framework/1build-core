package dev.onebuild.ui.config;

import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.service.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DomainConfiguration {

  @Bean("scriptParameters")
  public ScriptParameters scriptParameters() {
    return new ScriptParameters();
  }

  @Bean("scriptService")
  public ScriptService scriptService(
      OneBuildConfigs oneBuildConfigs,
      freemarker.template.Configuration freemarkerConfiguration,
      ScriptParameters scriptParameters) {
    return new ScriptService(oneBuildConfigs, freemarkerConfiguration, scriptParameters);
  }
}