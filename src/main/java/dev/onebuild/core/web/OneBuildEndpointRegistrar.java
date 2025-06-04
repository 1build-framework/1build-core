package dev.onebuild.core.web;

import dev.onebuild.commons.domain.model.ui.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

import static dev.onebuild.core.utils.AppUtils.getEndpoints;
import static dev.onebuild.core.utils.ResourceUtils.findResources;

@Slf4j
public class OneBuildEndpointRegistrar {
  private final OneBuildIndex oneBuildIndex;
  private final OneBuildValidator oneBuildValidator;
  private final List<OneBuildLocation> locations;
  private final List<OneBuildEndpoint> endpoints;
  private final RequestMappingHandlerMapping handlerMapping;
  private final HttpResourceHandler httpResourceHandler;
  private final HttpDatabaseHandler httpDatabaseHandler;

  public OneBuildEndpointRegistrar(OneBuildIndex oneBuildIndex,
                                   OneBuildValidator oneBuildValidator,
                                   List<OneBuildLocation> locations,
                                   List<OneBuildEndpoint> endpoints,
                                   RequestMappingHandlerMapping handlerMapping,
                                   HttpResourceHandler httpResourceHandler,
                                   HttpDatabaseHandler httpDatabaseHandler) {
    this.oneBuildIndex = oneBuildIndex;
    this.oneBuildValidator = oneBuildValidator;
    this.locations = locations;
    this.endpoints = endpoints;
    this.handlerMapping = handlerMapping;
    this.httpResourceHandler = httpResourceHandler;
    this.httpDatabaseHandler = httpDatabaseHandler;
  }

  public void init() throws Exception {
    initApplicationEndpoints();
    initDatabaseEndpoints();
  }

  private void initApplicationEndpoints() {
    //CSS
    findResources(locations, ResourceType.CSS).forEach(location -> {
      log.debug("Registering endpoint {}", location.getWebPath());
      try {
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(location.getWebPath() + "/{cssName}")
                .methods(RequestMethod.GET)
                .produces("text/css")
                .build(),
            httpResourceHandler,
            httpResourceHandler.getClass().getMethod("renderCssResource", HttpServletRequest.class, String.class));
      } catch (Exception e) {
        log.error("Error while registering CSS endpoint", e);
      }
    });

    //JS
    findResources(locations, ResourceType.JS).forEach(location -> {
      log.debug("Registering endpoint {}", location.getWebPath());
      try {
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(location.getWebPath() + "/{jsName}")
                .methods(RequestMethod.GET)
                .produces("text/javascript")
                .build(),
            httpResourceHandler,
            httpResourceHandler.getClass().getMethod("renderJsResource", HttpServletRequest.class, String.class));
      } catch (Exception e) {
        log.error("Error while registering CSS endpoint", e);
      }
    });

    //INDEX
    log.debug("INDEX endpoint {} for Source: {}", oneBuildIndex.getWebPath(), oneBuildIndex.getSourcePath());
    try {
      handlerMapping.registerMapping(
          RequestMappingInfo
              .paths(oneBuildIndex.getWebPath())
              .methods(RequestMethod.GET)
              .produces("text/html")
              .build(),
          httpResourceHandler,
          httpResourceHandler.getClass().getMethod("renderIndex"));
    } catch (Exception e) {
      log.error("Error while registering INDEX endpoint", e);
    }

    //SCRIPT
    findResources(locations, ResourceType.SCRIPT).forEach(location -> {
      log.debug("Registering endpoint {}", location.getWebPath());
      try {
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(location.getWebPath() + "/{scriptName}")
                .methods(RequestMethod.GET)
                .produces("text/javascript")
                .build(),
            httpResourceHandler,
            httpResourceHandler.getClass().getMethod("renderScript", HttpServletRequest.class, String.class));
      } catch (Exception e) {
        log.error("Error while registering SCRIPT endpoint", e);
      }
    });

    //VALIDATOR
    if(oneBuildValidator != null) {
      log.debug("VALIDATOR endpoint {}", oneBuildValidator.getWebPath());
      try {
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(oneBuildValidator.getWebPath())
                .methods(RequestMethod.GET)
                .produces("text/javascript")
                .build(),
            httpResourceHandler,
            httpResourceHandler.getClass().getMethod("renderValidator"));
      } catch (Exception e) {
        log.error("Error while registering INDEX endpoint", e);
      }
    }
  }

  private void initDatabaseEndpoints() throws Exception {
    for(OneBuildEndpoint endpoint : getEndpoints(endpoints)) {
      log.debug("Initializing endpoint for {}", endpoint.getWebPath());

      //FIND
      if(endpoint.getOperations().contains(DatabaseOpType.FIND)) {
        log.debug("Registering endpoint Method {}, URL {}", RequestMethod.GET, endpoint.getWebPath());
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(endpoint.getWebPath())
                .methods(RequestMethod.GET)
                .produces("application/json")
                .build(),
            httpDatabaseHandler,
            httpDatabaseHandler.getClass().getMethod("find", HttpServletRequest.class, MultiValueMap.class)
        );
      }

      //INSERT_ONE
      if(endpoint.getOperations().contains(DatabaseOpType.CREATE)) {
        log.debug("Registering endpoint Method {}, URL {}", RequestMethod.POST, endpoint.getWebPath());
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(endpoint.getWebPath())
                .methods(RequestMethod.POST)
                .produces("application/json")
                .build(),
            httpDatabaseHandler,
            httpDatabaseHandler.getClass().getMethod("create", HttpServletRequest.class, Map.class)
        );
      }

      //UPDATE
      if(endpoint.getOperations().contains(DatabaseOpType.UPDATE)) {
        log.debug("Registering endpoint Method {}, URL {}", RequestMethod.PUT, endpoint.getWebPath() + "/{id}");
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(endpoint.getWebPath() + "/{id}")
                .methods(RequestMethod.PUT)
                .produces("application/json")
                .build(),
            httpDatabaseHandler,
            httpDatabaseHandler.getClass().getMethod("update", HttpServletRequest.class, Long.class, Map.class)
        );
      }

      //DELETE
      if(endpoint.getOperations().contains(DatabaseOpType.DELETE)) {
        log.debug("Registering endpoint Method {}, URL {}", RequestMethod.DELETE, endpoint.getWebPath());
        handlerMapping.registerMapping(
            RequestMappingInfo
                .paths(endpoint.getWebPath())
                .methods(RequestMethod.DELETE)
                .produces("application/json")
                .build(),
            httpDatabaseHandler,
            httpDatabaseHandler.getClass().getMethod("delete", HttpServletRequest.class, String.class)
        );
      }
    }
  }
}