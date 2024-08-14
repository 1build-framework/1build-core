package dev.onebuild.ui.config;

import dev.onebuild.ui.config.model.ComponentsConfigs;
import dev.onebuild.ui.config.model.CssConfigs;
import dev.onebuild.ui.config.model.IndexConfigs;
import dev.onebuild.ui.config.model.JsConfigs;
import dev.onebuild.ui.domain.service.ScriptService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@Configuration
public class UiConfiguration {
  @Bean
  public List<TemplateLoader> templateLoaders(IndexConfigs indexConfigs,
                                              ComponentsConfigs componentsConfigs) {
    var templateLoaders = new ArrayList<TemplateLoader>();
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), componentsConfigs.getSourcePath()));

    templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfigs.getSourcePath()));
    if(!indexConfigs.getSourcePath().equalsIgnoreCase(indexConfigs.getTemplateSourcePath())) {
      templateLoaders.add(new ClassTemplateLoader(this.getClass(), indexConfigs.getTemplateSourcePath()));
    }
    return templateLoaders;
  }

  @Bean
  public freemarker.template.Configuration freemarkerConfiguration(List<TemplateLoader> templateLoaders) {
    var configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
    configuration.setTemplateLoader(new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[0])));
//    configuration.setClassForTemplateLoading(this.getClass(), indexConfigProperties.getClasspath());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(true);
    configuration.setWrapUncheckedExceptions(true);
    configuration.setFallbackOnNullLoopVariable(false);
    return configuration;
  }

  @Bean
  public ScriptService indexService(
      CssConfigs cssConfigs,
      JsConfigs jsConfigs,
      ComponentsConfigs componentsConfigs,
      IndexConfigs indexConfigs,
      freemarker.template.Configuration freemarkerConfiguration) {
    return new ScriptService(cssConfigs, jsConfigs, componentsConfigs, indexConfigs, freemarkerConfiguration);
  }
}
