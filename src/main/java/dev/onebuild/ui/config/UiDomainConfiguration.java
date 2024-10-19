package dev.onebuild.ui.config;

import dev.onebuild.domain.model.*;
import dev.onebuild.domain.model.ui.*;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.service.ScriptService;
import dev.onebuild.errors.OneBuildExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class UiDomainConfiguration {
  @Bean("scriptParameters")
  public ScriptParameters scriptParameters() {
    return new ScriptParameters();
  }

  @Bean("scriptService")
  public ScriptService scriptService(
      OneBuildExceptionFactory oneBuildExceptionFactory,
      OneBuildAppSettings oneBuildAppSettings,
      OneBuildIndex oneBuildIndex,
      @Nullable OneBuildValidator oneBuildValidator,
      List<OneBuildLocation> locations,
      @Qualifier("uiFreemarkerConfiguration") freemarker.template.Configuration uiFreemarkerConfiguration,
      ScriptParameters scriptParameters) {
    return new ScriptService(
        oneBuildExceptionFactory,
        oneBuildAppSettings,
        oneBuildIndex,
        oneBuildValidator,
        locations,
        uiFreemarkerConfiguration,
        scriptParameters);
  }
}