package dev.onebuild.ui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildLocation;
import dev.onebuild.domain.model.OneBuildResources;
import dev.onebuild.domain.model.ResourceType;
import dev.onebuild.utils.OneBuildExceptionFactory;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        .filter(resource -> StringUtils.isNotBlank(resource.getPath()) && StringUtils.isNotBlank(resource.getSourcePath()))
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
            OneBuildLocation::getPath,
            OneBuildLocation::getSourcePath,
            (existing, replacement) -> existing
        ));
  }

  public static Map<String, String> getComponents(List<OneBuildComponents> components,
                                                 OneBuildExceptionFactory exceptionFactory) {
    return components.stream()
        .filter(component -> StringUtils.isNotBlank(component.getPath()) && StringUtils.isNotBlank(component.getSourcePath()))
        .filter(component -> {
          if (component.getList().isEmpty()) {
            throw exceptionFactory.createMissingPropertyException(component);
          }
          return true;
        })
        .distinct()
        .collect(Collectors.toMap(
            OneBuildLocation::getPath,
            OneBuildLocation::getSourcePath,
            (existing, replacement) -> existing
        ));
  }
}