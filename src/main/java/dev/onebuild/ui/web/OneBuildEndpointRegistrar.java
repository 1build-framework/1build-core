package dev.onebuild.ui.web;

import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildIndex;
import dev.onebuild.domain.model.OneBuildResources;
import dev.onebuild.domain.model.ResourceType;
import dev.onebuild.utils.OneBuildExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static dev.onebuild.ui.utils.AppUtils.getComponents;
import static dev.onebuild.ui.utils.AppUtils.getLocations;

@Slf4j
public class OneBuildEndpointRegistrar {

  private final OneBuildExceptionFactory exceptionFactory;

  private final OneBuildIndex oneBuildIndex;

  private final List<OneBuildResources> resources;

  private final List<OneBuildComponents> components;

  private final RequestMappingHandlerMapping handlerMapping;

  private final HttpHandler httpHandler;

  public OneBuildEndpointRegistrar(OneBuildIndex oneBuildIndex,
                                   List<OneBuildResources> resources,
                                   List<OneBuildComponents> components,
                                   RequestMappingHandlerMapping handlerMapping,
                                   HttpHandler httpHandler,
                                   OneBuildExceptionFactory exceptionFactory) {
    this.oneBuildIndex = oneBuildIndex;
    this.resources = resources;
    this.components = components;
    this.handlerMapping = handlerMapping;
    this.httpHandler = httpHandler;
    this.exceptionFactory = exceptionFactory;
  }

  public void init() throws Exception {
    //css controller method
    Method renderResourceMethod = httpHandler.getClass().getMethod("renderCssResource", HttpServletRequest.class, String.class);
    Map<String, String> cssPaths = getLocations(resources, ResourceType.CSS, exceptionFactory);
    cssPaths.forEach((path, sourcePath) -> {
      log.debug("CSS endpoint {} for Source: {}", path, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(path + "/{cssName}")
            .methods(RequestMethod.GET)
            .produces("text/css")
            .build(),
        httpHandler,
        renderResourceMethod
      );
    });

    //js controller method
    Method renderJsResourceMethod = httpHandler.getClass().getMethod("renderJsResource", HttpServletRequest.class, String.class);
    Map<String, String> jsPaths = getLocations(resources, ResourceType.JS, exceptionFactory);
    jsPaths.forEach((jsPath, sourcePath) -> {
      log.debug("JS endpoint {} for Source: {}", jsPath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(jsPath + "/{jsName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpHandler,
        renderJsResourceMethod
      );
    });

    //component controller method
    Method renderComponentMethod = httpHandler.getClass().getMethod("renderComponent", String.class);
    Map<String, String> componentPaths = getComponents(components, exceptionFactory);
    componentPaths.forEach((componentPath, sourcePath) -> {
      log.debug("COMPONENT endpoint {} for Source: {}", componentPath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(componentPath + "/{componentName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpHandler,
        renderComponentMethod
      );
    });

    //service controller method
    Method renderServiceMethod = httpHandler.getClass().getMethod("renderService", String.class);
    Map<String, String> servicePaths = getLocations(resources, ResourceType.SERVICE, exceptionFactory);
    servicePaths.forEach((servicePath, sourcePath) -> {
      log.debug("SERVICE endpoint {} for Source: {}", servicePath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(servicePath + "/{serviceName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpHandler,
        renderServiceMethod
      );
    });

    //store controller method
    Method renderStoreMethod = httpHandler.getClass().getMethod("renderStore", String.class);
    Map<String, String> storePaths = getLocations(resources, ResourceType.STORE, exceptionFactory);
    storePaths.forEach((storePath, sourcePath) -> {
      log.debug("STORE endpoint {} for Source: {}", storePath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(storePath + "/{storeName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpHandler,
        renderStoreMethod
      );
    });

    //index controller method
    Method renderIndexMethod = httpHandler.getClass().getMethod("renderIndex");
    log.debug("INDEX endpoint {} for Source: {}", oneBuildIndex.getPath(), oneBuildIndex.getSourcePath());
    handlerMapping.registerMapping(
      RequestMappingInfo
          .paths(oneBuildIndex.getPath())
          .methods(RequestMethod.GET)
          .produces("text/html")
          .build(),
      httpHandler,
      renderIndexMethod
    );
  }
}
