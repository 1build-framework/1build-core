package dev.onebuild.ui.web;

import dev.onebuild.domain.model.ui.*;
import dev.onebuild.utils.OneBuildExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static dev.onebuild.ui.utils.AppUtils.*;

@Slf4j
public class OneBuildEndpointRegistrar {

  private final OneBuildExceptionFactory exceptionFactory;

  private final OneBuildIndex oneBuildIndex;

  private final List<OneBuildResources> resources;
  private final List<OneBuildLocation> locations;

  private final List<OneBuildComponents> components;

  private final List<OneBuildEndpoint> endpoints;
  private final RequestMappingHandlerMapping handlerMapping;

  private final HttpResourceHandler httpResourceHandler;
  private final HttpDatabaseHandler httpDatabaseHandler;

  public OneBuildEndpointRegistrar(OneBuildIndex oneBuildIndex,
                                   List<OneBuildResources> resources,
                                   List<OneBuildLocation> locations,
                                   List<OneBuildComponents> components,
                                   List<OneBuildEndpoint> endpoints,
                                   RequestMappingHandlerMapping handlerMapping,
                                   HttpResourceHandler httpResourceHandler,
                                   HttpDatabaseHandler httpDatabaseHandler,
                                   OneBuildExceptionFactory exceptionFactory) {
    this.oneBuildIndex = oneBuildIndex;
    this.resources = resources;
    this.locations = locations;
    this.components = components;
    this.endpoints = endpoints;
    this.handlerMapping = handlerMapping;
    this.httpResourceHandler = httpResourceHandler;
    this.httpDatabaseHandler = httpDatabaseHandler;
    this.exceptionFactory = exceptionFactory;
  }

  private void initResources(Method method, ResourceType resourceType, String name, RequestMethod requestMethod, String contentType) {
    log.debug("Initializing endpoint for {} with method {}", resourceType, method.getName());
    Map<String, String> resourcePaths = getLocations(resources, resourceType, exceptionFactory);
    initResources(method, resourcePaths, name, requestMethod, contentType);
  }

  private void initLocations(Method method, ResourceType resourceType, String name, RequestMethod requestMethod, String contentType) {
    log.debug("Initializing endpoint for {} with method {}", resourceType, method.getName());
    Map<String, String> resourcePaths = getLocations(locations, resourceType);
    initResources(method, resourcePaths, name, requestMethod, contentType);
  }

  private void initResources(Method method, Map<String, String> resourcePaths, String name, RequestMethod requestMethod, String contentType) {
    log.debug("Initializing endpoint for method {}", method.getName());
    resourcePaths.forEach((path, sourcePath) -> {
      log.debug("Resource endpoint {} for Source: {}", path, sourcePath);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(path + name)
              .methods(requestMethod)
              .produces(contentType)
              .build(),
          httpResourceHandler,
          method
      );
    });
  }

  public void init() throws Exception {
    //css controller method
    Method renderResourceMethod = httpResourceHandler.getClass().getMethod("renderCssResource", HttpServletRequest.class, String.class);
    initResources(renderResourceMethod, ResourceType.CSS, "/{cssName}", RequestMethod.GET, "text/css");

    //js controller method
    renderResourceMethod = httpResourceHandler.getClass().getMethod("renderJsResource", HttpServletRequest.class, String.class);
    initResources(renderResourceMethod, ResourceType.JS, "/{jsName}", RequestMethod.GET, "text/javascript");

    //component controller method
    renderResourceMethod = httpResourceHandler.getClass().getMethod("renderComponent", String.class);
    Map<String, String> componentPaths = getComponents(components, exceptionFactory);
    initResources(renderResourceMethod, componentPaths, "/{componentName}", RequestMethod.GET, "text/javascript");

    //service controller method
    renderResourceMethod = httpResourceHandler.getClass().getMethod("renderService", String.class);
    initLocations(renderResourceMethod, ResourceType.SERVICE, "/{serviceName}", RequestMethod.GET, "text/javascript");

    //store controller method
    renderResourceMethod = httpResourceHandler.getClass().getMethod("renderStore", String.class);
    initLocations(renderResourceMethod, ResourceType.STORE, "/{storeName}", RequestMethod.GET, "text/javascript");

    //index controller method
    Method renderIndexMethod = httpResourceHandler.getClass().getMethod("renderIndex");
    log.debug("INDEX endpoint {} for Source: {}", oneBuildIndex.getPath(), oneBuildIndex.getSourcePath());
    handlerMapping.registerMapping(
      RequestMappingInfo
          .paths(oneBuildIndex.getPath())
          .methods(RequestMethod.GET)
          .produces("text/html")
          .build(),
        httpResourceHandler,
      renderIndexMethod
    );
    initDatabaseEndpoints();
  }

  private void initDatabaseEndpoints() throws Exception {
    for(OneBuildEndpoint endpoint : getEndpoints(endpoints)) {
      log.debug("Initializing endpoint for {}", endpoint.getPath());

      //FIND_BY_ID
      Method findByIdMethod = httpDatabaseHandler.getClass().getMethod("findById", HttpServletRequest.class, Long.class);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(endpoint.getPath() + "/{id}")
              .methods(RequestMethod.GET)
              .produces("application/json")
              .build(),
          httpDatabaseHandler,
          findByIdMethod
      );

      //FIND_ALL
      Method findAllMethod = httpDatabaseHandler.getClass().getMethod("findAll", HttpServletRequest.class);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(endpoint.getPath())
              .methods(RequestMethod.GET)
              .produces("application/json")
              .build(),
          httpDatabaseHandler,
          findAllMethod
      );

      //INSERT_ONE
      Method saveMethod = httpDatabaseHandler.getClass().getMethod("save", HttpServletRequest.class, Map.class);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(endpoint.getPath())
              .methods(RequestMethod.POST)
              .produces("application/json")
              .build(),
          httpDatabaseHandler,
          saveMethod
      );

      //UPDATE_BY_ID
      Method updateMethod = httpDatabaseHandler.getClass().getMethod("updateById", HttpServletRequest.class, Long.class, Map.class);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(endpoint.getPath() + "/{id}")
              .methods(RequestMethod.PUT)
              .produces("application/json")
              .build(),
          httpDatabaseHandler,
          updateMethod
      );

      //DELETE_BY_ID
      Method deleteById = httpDatabaseHandler.getClass().getMethod("deleteById", HttpServletRequest.class, Long.class);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(endpoint.getPath() + "/{id}")
              .methods(RequestMethod.DELETE)
              .produces("application/json")
              .build(),
          httpDatabaseHandler,
          deleteById
      );
    }
  }
}


    /*Map<String, String> jsPaths = getLocations(resources, ResourceType.JS, exceptionFactory);
    jsPaths.forEach((jsPath, sourcePath) -> {
      log.debug("JS endpoint {} for Source: {}", jsPath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(jsPath + "/{jsName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpResourceHandler,
        renderJsResourceMethod
      );
    });
    Map<String, String> cssPaths = getLocations(resources, ResourceType.CSS, exceptionFactory);
    cssPaths.forEach((path, sourcePath) -> {
      log.debug("CSS endpoint {} for Source: {}", path, sourcePath);
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(path + "/{cssName}")
              .methods(RequestMethod.GET)
              .produces("text/css")
              .build(),
          httpResourceHandler,
          renderResourceMethod
      );
    });

    Method renderComponentMethod = httpResourceHandler.getClass().getMethod("renderComponent", String.class);
    Map<String, String> componentPaths = getComponents(components, exceptionFactory);
    componentPaths.forEach((componentPath, sourcePath) -> {
      log.debug("COMPONENT endpoint {} for Source: {}", componentPath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(componentPath + "/{componentName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpResourceHandler,
        renderComponentMethod
      );
    });

        Map<String, String> servicePaths = getLocations(resources, ResourceType.SERVICE, exceptionFactory);
    servicePaths.forEach((servicePath, sourcePath) -> {
      log.debug("SERVICE endpoint {} for Source: {}", servicePath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(servicePath + "/{serviceName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpResourceHandler,
        renderServiceMethod
      );
    });

    Method renderStoreMethod = httpResourceHandler.getClass().getMethod("renderStore", String.class);
    Map<String, String> storePaths = getLocations(resources, ResourceType.STORE, exceptionFactory);
    storePaths.forEach((storePath, sourcePath) -> {
      log.debug("STORE endpoint {} for Source: {}", storePath, sourcePath);
      handlerMapping.registerMapping(
        RequestMappingInfo
            .paths(storePath + "/{storeName}")
            .methods(RequestMethod.GET)
            .produces("text/javascript")
            .build(),
        httpResourceHandler,
        renderStoreMethod
      );
    });

*/
