package dev.onebuild.ui.config;

import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildIndex;
import dev.onebuild.domain.model.OneBuildResources;
import dev.onebuild.domain.model.ResourceType;
import dev.onebuild.ui.domain.service.ScriptService;
import dev.onebuild.ui.web.HttpHandler;
import dev.onebuild.ui.web.OneBuildEndpointRegistrar;
import dev.onebuild.utils.OneBuildExceptionFactory;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dev.onebuild.ui.utils.AppUtils.getLocations;

@Configuration
public class WebConfiguration {
  @Bean
  public HttpHandler httpHandler(@Value("${onebuild.production.enabled:false}") boolean prodEnabled,
                                 List<OneBuildResources> resources,
                                 ScriptService scriptService,
                                 OneBuildExceptionFactory exceptionFactory) {
    return new HttpHandler(prodEnabled, resources, scriptService, exceptionFactory);
  }

  @Bean(name = "oneBuildEndpointRegistrar", initMethod = "init")
  public OneBuildEndpointRegistrar oneBuildEndpointRegistrar(OneBuildIndex oneBuildIndex,
                                                             List<OneBuildResources> resources,
                                                             List<OneBuildComponents> components,
                                                             @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                                                             HttpHandler httpHandler,
                                                             OneBuildExceptionFactory exceptionFactory) {
    return new OneBuildEndpointRegistrar(oneBuildIndex, resources, components, handlerMapping, httpHandler, exceptionFactory);
  }

}
