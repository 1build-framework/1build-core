package dev.onebuild.ui.config;

import dev.onebuild.domain.model.*;
import dev.onebuild.utils.OneBuildExceptionFactory;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.onebuild.ui.utils.AppUtils.getComponents;
import static dev.onebuild.ui.utils.AppUtils.getLocations;

//@ConditionalOnProperty(value = "onebuild.ui")
@Slf4j
@Configuration
public class UiTemplateConfiguration {
  @Bean("uiTemplateLoaders")
  public List<TemplateLoader> uiTemplateLoaders(OneBuildIndex oneBuildIndex,
                                                List<OneBuildResources> resources,
                                                List<OneBuildComponents> components,
                                                OneBuildExceptionFactory exceptionFactory) {
    var templateLoaders = new ArrayList<TemplateLoader>();

    //Index Classpath
    log.info("Index Path {}, Source Path: {}", oneBuildIndex.getPath(), oneBuildIndex.getSourcePath());
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), oneBuildIndex.getSourcePath()));

    //All resources
    Map<String, String> resourceMappings = getLocations(resources, null, exceptionFactory);
    resourceMappings.forEach((path, sourcePath) -> {
      log.info("Resource Path {}, Source Path: {}", path, sourcePath);
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), sourcePath));
    });

    //All components
    Map<String, String> componentMappings = getComponents(components, exceptionFactory);
    componentMappings.forEach((path, sourcePath) -> {
      log.info("Components Path {}, Source Path: {}", path, sourcePath);
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