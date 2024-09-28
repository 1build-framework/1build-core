package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.*;
import dev.onebuild.utils.OneBuildExceptionFactory;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.onebuild.ui.utils.AppUtils.getComponents;
import static dev.onebuild.ui.utils.AppUtils.getLocations;

//@ConditionalOnProperty(value = "onebuild.ui")
@Slf4j
@Configuration
public class UiTemplateConfiguration {
  @Bean("uiTemplateLoaders")
  public List<TemplateLoader> uiTemplateLoaders(OneBuildIndex oneBuildIndex,
                                                List<OneBuildComponents> components,
                                                List<OneBuildLocation> locations,
                                                OneBuildExceptionFactory exceptionFactory) {
    var templateLoaders = new ArrayList<TemplateLoader>();

    //Index Classpath
    log.info("Index Path {}, Source Path: {}", oneBuildIndex.getPath(), oneBuildIndex.getSourcePath());
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), oneBuildIndex.getSourcePath()));

/*
    //Javascript resources
    Map<String, String> javascriptMappings = getLocations(resources, ResourceType.JS, exceptionFactory);
    javascriptMappings.forEach((path, sourcePath) -> {
      log.info("Javascript Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });

    //Javascript resources
    Map<String, String> javascriptMappings = getLocations(resources, ResourceType.JS, exceptionFactory);
    javascriptMappings.forEach((path, sourcePath) -> {
      log.info("Javascript Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });
*/

    //All components
    Map<String, String> componentMappings = getComponents(components, exceptionFactory);
    componentMappings.forEach((path, sourcePath) -> {
      log.info("Components Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });

    //Service resources
    Map<String, String> serviceMappings = getLocations(locations, ResourceType.SERVICE);
    serviceMappings.forEach((path, sourcePath) -> {
      log.info("Service Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });

    //Store resources
    Map<String, String> storeMappings = getLocations(locations, ResourceType.STORE);
    storeMappings.forEach((path, sourcePath) -> {
      log.info("Store Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });

    return templateLoaders;
  }

  @Bean("uiFreemarkerConfiguration")
  public freemarker.template.Configuration uiFreemarkerConfiguration(@Qualifier("uiTemplateLoaders") List<TemplateLoader> uiTemplateLoaders) {
    var configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
    configuration.setTemplateLoader(new MultiTemplateLoader(uiTemplateLoaders.toArray(new TemplateLoader[0])));
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(true);
    configuration.setWrapUncheckedExceptions(true);
    configuration.setFallbackOnNullLoopVariable(false);
    return configuration;
  }
}