package dev.onebuild.ui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
