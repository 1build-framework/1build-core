package dev.onebuild.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.commons.domain.model.ui.*;
import dev.onebuild.commons.errors.OneBuildExceptionFactory;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.onebuild.core.utils.ResourceUtils.readResource;

public class AppUtils {
  private static final Logger log = LoggerFactory.getLogger(AppUtils.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      log.error("Error while serializing object to JSON", e);
    }
    return "";
  }

  public static Map<String, String> getLocations(List<OneBuildResources> resources,
                                                 ResourceType resourceType,
                                                 OneBuildExceptionFactory exceptionFactory) {
    return resources.stream()
        .filter(resource -> StringUtils.isNotBlank(resource.getWebPath()) && StringUtils.isNotBlank(resource.getSourcePath()))
        .filter(resource -> resourceType == null || resource.getResourceType() == resourceType)
        .filter(resource -> {
          if(
              (resource.getResourceType() == ResourceType.CSS || resource.getResourceType() == ResourceType.JS) &&
                  resource.getResources().isEmpty()
          ) {
            throw exceptionFactory.createMissingPropertyException(resource);
          }

          return true;
        })
        .distinct()
        .collect(Collectors.toMap(
            OneBuildLocation::getWebPath,
            OneBuildLocation::getSourcePath,
            (existing, replacement) -> existing
        ));
  }

  public static Map<String, String> getLocations(List<OneBuildLocation> locations,
                                                 ResourceType resourceType) {
    return locations.stream()
        .filter(resource -> StringUtils.isNotBlank(resource.getWebPath()) && StringUtils.isNotBlank(resource.getSourcePath()))
        .filter(resource -> resourceType == null || resource.getResourceType() == resourceType)
        .distinct()
        .collect(Collectors.toMap(
            OneBuildLocation::getWebPath,
            OneBuildLocation::getSourcePath,
            (existing, replacement) -> existing
        ));
  }

  public static Set<OneBuildEndpoint> getEndpoints(List<OneBuildEndpoint> endpoints) {
    return endpoints.stream()
        .filter(endpoint -> endpoint.getResourceType() == ResourceType.ENDPOINT)
        .filter(endpoint -> StringUtils.isNotBlank(endpoint.getWebPath()))
        .filter(endpoint -> StringUtils.isNotBlank(endpoint.getDataSource()))
        .filter(endpoint -> StringUtils.isNotBlank(endpoint.getSchema()))
        .filter(endpoint -> StringUtils.isNotBlank(endpoint.getTable()))
        .filter(endpoint -> StringUtils.isNotBlank(endpoint.getId()))
        .collect(Collectors.toSet());
  }

  public static Map<String, Object> processPagelets(String sourcePath, UiScript script) {
    List<UiPagelet> pagelets = script.getPagelets();

    Map<String, Object> model = new HashMap<>();
    if(pagelets == null || pagelets.isEmpty()) {
      return model;
    }

    pagelets.stream()
      .filter(pagelet -> StringUtils.isNotBlank(pagelet.getName()))
      .filter(pagelet -> StringUtils.isNotBlank(pagelet.getValue()))
      .forEach(pagelet -> {
        if (pagelet.getType() == PageletType.TEXT) {
          model.put(pagelet.getName(), pagelet.getValue());
        } else if(pagelet.getType() == PageletType.FILE) {
          if(pagelet.getValue().startsWith("/")) {
            model.put(pagelet.getName(), readResource(pagelet.getValue()));
          } else {
            model.put(pagelet.getName(), readResource(sourcePath + script.getHome() + "/" + pagelet.getValue()));
          }
        }
      });
    return model;
  }
}