package dev.onebuild.core.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;

@Configuration
public class UiUtilityConfiguration {

  @Bean
  public static OneBuildCoreConfigs coreConfigs(@Qualifier("yamlMapper") YAMLMapper yamlMapper) {
    Resource resource = new ClassPathResource("1build-ui.yml");
    OneBuildCoreConfigs configs;
    try {
      Map<String, Object> yamlMap = yamlMapper.readValue(resource.getInputStream(), Map.class);

      Map<String, Object> onebuild = (Map<String, Object>) yamlMap.get("onebuild");
      Map<String, Object> ui = (Map<String, Object>) onebuild.get("ui");
      Map<String, Object> core = (Map<String, Object>) ui.get("core");

      configs = yamlMapper.convertValue(core, OneBuildCoreConfigs.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load YAML configuration", e);
    }
    return configs;
  }
}