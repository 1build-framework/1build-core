package dev.onebuild.ui.config;

import dev.onebuild.ui.domain.model.config.OneBuildUiConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UiApplicationConfiguration {

  @Bean("version-store")
  public Map<String, Object> versionStore(OneBuildUiConfigs oneBuildUiConfigs) {

    final var libVersions = new HashMap<String, Object>();
    oneBuildUiConfigs.getJs().getJavascript().forEach((key, value) ->
      libVersions.put(key, getVersionFromName(value))
    );
    libVersions.put("spring-boot", "3.3.2");
    //scriptParameters.addParameter("libVersions", libVersions);

    var appInfo = new HashMap<String, Object>();
    appInfo.put("name", "1Build Fullstack Development Framework");
    appInfo.put("description", "1Build is a fullstack development framework based on Spring Boot and Vue with full power.");
    appInfo.put("libVersions", libVersions);

    return appInfo;
  }

  private String getVersionFromName(String name) {
    return name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
  }
}