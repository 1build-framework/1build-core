package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.*;
import dev.onebuild.domain.service.OneBuildDataService;
import dev.onebuild.ui.domain.service.ScriptService;
import dev.onebuild.ui.web.HttpDatabaseHandler;
import dev.onebuild.ui.web.HttpResourceHandler;
import dev.onebuild.ui.web.OneBuildEndpointRegistrar;
import dev.onebuild.errors.OneBuildExceptionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Optional;

@Configuration
public class WebConfiguration {
  @Bean
  public HttpResourceHandler httpHandler(@Value("${onebuild.production.enabled:false}") boolean prodEnabled,
                                         List<OneBuildResources> resources,
                                         ScriptService scriptService,
                                         OneBuildExceptionFactory exceptionFactory) {
    return new HttpResourceHandler(prodEnabled, resources, scriptService, exceptionFactory);
  }

  @Bean
  public HttpDatabaseHandler httpDatabaseHandler(
                                         Optional<OneBuildDataService> oneBuildDataService) {
    return new HttpDatabaseHandler(oneBuildDataService.orElse(null));
  }

  @Bean(name = "oneBuildEndpointRegistrar", initMethod = "init")
  public OneBuildEndpointRegistrar oneBuildEndpointRegistrar(OneBuildIndex oneBuildIndex,
                                                             List<OneBuildResources> resources,
                                                             List<OneBuildLocation> locations,
                                                             List<OneBuildComponents> components,
                                                             List<OneBuildEndpoint> endpoints,
                                                             @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                                                             HttpResourceHandler httpResourceHandler,
                                                             HttpDatabaseHandler httpDatabaseHandler,
                                                             OneBuildExceptionFactory exceptionFactory) {
    return new OneBuildEndpointRegistrar(oneBuildIndex,
        resources,
        locations,
        components,
        endpoints,
        handlerMapping,
        httpResourceHandler,
        httpDatabaseHandler,
        exceptionFactory);
  }
}
