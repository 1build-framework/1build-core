package dev.onebuild.ui.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ResourceUtils {

  /*public static String readResource(String classpath, String resourceName) {
    try {
      ClassPathResource classPathResource = new ClassPathResource(classpath + "/" + resourceName);
      byte[] dataArr = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
      return new String(dataArr, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return "";
    }
  }*/

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

}
