package dev.onebuild.ui.config;

import dev.onebuild.ui.domain.model.config.*;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class UiTemplateConfiguration {

  @Bean("uiTemplateLoaders")
  public List<TemplateLoader> uiTemplateLoaders(OneBuildUiConfigs oneBuildUiConfigs) {
    IndexConfig indexConfig = oneBuildUiConfigs.getIndex();
    ComponentsConfig componentsConfig = oneBuildUiConfigs.getComponent();
    ServiceConfig serviceConfig = oneBuildUiConfigs.getService();
    StoreConfig storeConfig = oneBuildUiConfigs.getStore();

    var templateLoaders = new ArrayList<TemplateLoader>();

    //Components Classpath
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), componentsConfig.getSourcePath()));
    log.info("Components Template Source Path: {}", componentsConfig.getSourcePath());

    //Index Classpath
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfig.getSourcePath()));
    if(!indexConfig.getSourcePath().equalsIgnoreCase(indexConfig.getTemplateSourcePath())) {
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfig.getTemplateSourcePath()));
    }
    log.info("Index Template Source Path: {}", indexConfig.getSourcePath());

    //Service Classpath
    if(serviceConfig != null) {
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), serviceConfig.getSourcePath()));
      log.info("Service Template Source Path: {}", serviceConfig.getSourcePath());
    }
    //Store Classpath
    if(storeConfig != null) {
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), storeConfig.getSourcePath()));
      log.info("Store Template Source Path: {}", storeConfig.getSourcePath());
    }

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
