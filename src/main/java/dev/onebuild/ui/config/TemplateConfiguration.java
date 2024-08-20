package dev.onebuild.ui.config;

import dev.onebuild.ui.domain.model.config.ComponentsConfigs;
import dev.onebuild.ui.domain.model.config.IndexConfigs;
import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
import dev.onebuild.ui.domain.model.config.ServiceConfigs;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class TemplateConfiguration {

  @Bean("templateLoaders")
  public List<TemplateLoader> templateLoaders(OneBuildConfigs oneBuildConfigs) {
    IndexConfigs indexConfigs = oneBuildConfigs.getIndex();
    ComponentsConfigs componentsConfigs = oneBuildConfigs.getComponents();
    ServiceConfigs serviceConfigs = oneBuildConfigs.getService();

    var templateLoaders = new ArrayList<TemplateLoader>();

    //Components Classpath
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), componentsConfigs.getSourcePath()));
    log.info("Components Template Source Path: {}", componentsConfigs.getSourcePath());

    //Index Classpath
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfigs.getSourcePath()));
    if(!indexConfigs.getSourcePath().equalsIgnoreCase(indexConfigs.getTemplateSourcePath())) {
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfigs.getTemplateSourcePath()));
    }
    log.info("Index Template Source Path: {}", indexConfigs.getSourcePath());

    //Service Classpath
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), serviceConfigs.getSourcePath()));
    log.info("Service Template Source Path: {}", serviceConfigs.getSourcePath());

    return templateLoaders;
  }

  @Bean("freemarkerConfiguration")
  public freemarker.template.Configuration freemarkerConfiguration(List<TemplateLoader> templateLoaders) {
    var configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
    configuration.setTemplateLoader(new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[0])));
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(true);
    configuration.setWrapUncheckedExceptions(true);
    configuration.setFallbackOnNullLoopVariable(false);
    return configuration;
  }
}
