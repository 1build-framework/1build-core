package dev.onebuild.ui.config;

import dev.onebuild.domain.model.*;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.service.ScriptService;
import dev.onebuild.utils.OneBuildExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
      List<OneBuildResources> resources,
      List<OneBuildComponents> components,
      ApplicationContext applicationContext,
      @Qualifier("uiFreemarkerConfiguration") freemarker.template.Configuration uiFreemarkerConfiguration,
      ScriptParameters scriptParameters) {
    return new ScriptService(
        oneBuildExceptionFactory,
        oneBuildAppSettings,
        oneBuildIndex,
        resources,
        components,
        uiFreemarkerConfiguration,
        scriptParameters,
        applicationContext);
  }

  @Bean("version-store")
  public Map<String, Object> versionStore(List<OneBuildResources> resources) {
    final var libVersions = new HashMap<String, Object>();
    resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.JS)
        .forEach(jsResource -> {
          jsResource.getResources().forEach(value -> {
            libVersions.put(getName(value), getVersion(value));
          });
        });
    libVersions.put("spring-boot", "3.3.2");

    var appInfo = new HashMap<String, Object>();
    appInfo.put("name", "1Build Fullstack Development Framework");
    appInfo.put("description", "1Build is a fullstack development framework based on Spring Boot and Vue with full power.");
    appInfo.put("libVersions", libVersions);

    return appInfo;
  }

  private String getName(String name) {
    return name.substring(0, name.lastIndexOf("-"));
  }

  private String getVersion(String name) {
    return name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
  }
}