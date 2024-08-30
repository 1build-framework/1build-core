package dev.onebuild.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.ui.domain.model.config.OneBuildUiConfigs;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.service.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class UiDomainConfiguration {
  @Bean("scriptParameters")
  public ScriptParameters scriptParameters() {
    return new ScriptParameters();
  }

  @Bean("scriptService")
  public ScriptService scriptService(
      ObjectMapper mapper,
      ApplicationContext applicationContext,
      OneBuildUiConfigs oneBuildUiConfigs,
      @Qualifier("uiFreemarkerConfiguration") freemarker.template.Configuration uiFreemarkerConfiguration,
      ScriptParameters scriptParameters) {
    return new ScriptService(mapper,
        oneBuildUiConfigs,
        uiFreemarkerConfiguration,
        scriptParameters,
        applicationContext);
  }
}