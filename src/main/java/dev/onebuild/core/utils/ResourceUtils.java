package dev.onebuild.core.utils;

import dev.onebuild.commons.domain.model.ui.OneBuildLocation;
import dev.onebuild.commons.domain.model.ui.ResourceType;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ResourceUtils {
  private static final List<String> helperList = Arrays.asList(
      "vue", "vuedemi", "vuetify", "vue-router", "pinia", "axios"
  );

  public static List<OneBuildLocation> findResources(List<OneBuildLocation> locations, ResourceType resourceType) {
    return locations.stream()
        .filter(location -> (resourceType == null || location.getResourceType() == resourceType))
        .filter(location -> StringUtils.isNotBlank(location.getWebPath()) && StringUtils.isNotBlank(location.getSourcePath()))
        .collect(Collectors.toList());
  }

  public static String readResource(String resourceName) {
    try {
      ClassPathResource resource = new ClassPathResource(resourceName);
      if (resource.exists()) {
        byte[] contents = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(contents, StandardCharsets.UTF_8);
      } else {
        log.error("Resource {} does not exists", resourceName);
        return "";
      }
    } catch (Exception e) {
      log.error("Error reading resource " + resourceName, e.getMessage());
      return "";
    }
  }

  public static String readResource(boolean prodEnabled, String classpath, String resourceName) {
    try {
      if(prodEnabled) {
        return readProd(classpath, resourceName);
      }
      return readDev(classpath, resourceName);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return "";
    }
  }

  private static String readDev(String classpath, String resourceName) throws Exception {
    String contents = null;
    int extensionIndex = resourceName.lastIndexOf('.');
    if (extensionIndex != -1) {
      String devResourceName = resourceName.substring(0, extensionIndex) + ".dev" + resourceName.substring(extensionIndex);

      ClassPathResource devResource = new ClassPathResource(classpath + "/" + devResourceName);
      if (devResource.exists()) {
        byte[] devDataArr = FileCopyUtils.copyToByteArray(devResource.getInputStream());
        contents = new String(devDataArr, StandardCharsets.UTF_8);
      } else {
        contents = readProd(classpath, resourceName);
      }
    }
    return contents;
  }

  private static String readProd(String classpath, String resourceName) throws Exception {
    ClassPathResource classPathResource = new ClassPathResource(classpath + "/" + resourceName);
    byte[] dataArr = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
    return new String(dataArr, StandardCharsets.UTF_8);
  }

  public static void sortResources(List<String> resources) {
    resources.sort(Comparator.comparingInt(resource -> {
      // Find the index in helperList based on the prefix of the resource name
      for (int i = 0; i < helperList.size(); i++) {
        if (resource.startsWith(helperList.get(i))) {
          return i;
        }
      }
      // If not found in helperList, put it at the end
      return Integer.MAX_VALUE;
    }));
  }
}