package dev.onebuild.ui.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ResourceUtils {

  public static String readResource(String classpath, String resourceName) {
    try {
      ClassPathResource classPathResource = new ClassPathResource(classpath + "/" + resourceName);
      byte[] dataArr = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
      return new String(dataArr, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return "";
    }
  }
}
