package dev.onebuild.core.config;

import dev.onebuild.commons.domain.model.ui.*;
import dev.onebuild.commons.domain.service.OneBuildDataService;
import dev.onebuild.core.domain.service.ScriptService;
import dev.onebuild.core.web.HttpDatabaseHandler;
import dev.onebuild.core.web.HttpResourceHandler;
import dev.onebuild.core.web.OneBuildEndpointRegistrar;
import dev.onebuild.commons.errors.OneBuildExceptionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
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
                                                             List<OneBuildLocation> locations,
                                                             List<OneBuildEndpoint> endpoints,
                                                             @Nullable OneBuildValidator oneBuildValidator,
                                                             @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                                                             HttpResourceHandler httpResourceHandler,
                                                             HttpDatabaseHandler httpDatabaseHandler) {
    return new OneBuildEndpointRegistrar(oneBuildIndex,
        oneBuildValidator,
        locations,
        endpoints,
        handlerMapping,
        httpResourceHandler,
        httpDatabaseHandler);
  }
}
