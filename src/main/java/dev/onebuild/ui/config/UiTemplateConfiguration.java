package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.OneBuildIndex;
import dev.onebuild.domain.model.ui.OneBuildLocation;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static dev.onebuild.ui.utils.ResourceUtils.findResources;

@Slf4j
@Configuration
public class UiTemplateConfiguration {
  private static final String VALIDATOR_PATH = "/internal/ui/validator";

  @Bean("uiTemplateLoaders")
  public List<TemplateLoader> uiTemplateLoaders(OneBuildIndex oneBuildIndex,
                                                List<OneBuildLocation> locations) {
    var templateLoaders = new ArrayList<TemplateLoader>();

    //Index Classpath
    log.info("Index Path {}, Source Path: {}", oneBuildIndex.getWebPath(), oneBuildIndex.getSourcePath());
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), oneBuildIndex.getSourcePath()));

    //Validator Classpath
    log.info("Validator Path {}", VALIDATOR_PATH);
    templateLoaders.add(new ClassTemplateLoader(this.getClass(), VALIDATOR_PATH));

    //All Locations
    findResources(locations, null).stream()
        .map(OneBuildLocation::getSourcePath)
        .distinct()
        .forEach((sourcePath) -> {
          log.info("Registering Components Source Path: {}", sourcePath);
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