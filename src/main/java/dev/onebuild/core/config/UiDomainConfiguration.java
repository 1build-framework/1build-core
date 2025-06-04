package dev.onebuild.core.config;

import dev.onebuild.commons.domain.model.OneBuildAppSettings;
import dev.onebuild.commons.domain.model.ui.OneBuildIndex;
import dev.onebuild.commons.domain.model.ui.OneBuildLocation;
import dev.onebuild.commons.domain.model.ui.OneBuildValidator;
import dev.onebuild.core.domain.model.config.ScriptParameters;
import dev.onebuild.core.domain.service.ScriptService;
import dev.onebuild.commons.errors.OneBuildExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.List;

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