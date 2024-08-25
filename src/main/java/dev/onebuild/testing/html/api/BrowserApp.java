package dev.onebuild.testing.html.api;

public interface BrowserApp {
  boolean exists(String id);
  void click(String id);
  void setInputValue(String id, String value);
  void setSelectValue(String id, String value);
  String getInputValue(String id);
  MultiValueData getMultiValueData(String id);
  void takeScreenshot(String screenshotName);
  void savePageSource();
  String getLogs();
  void printLogs();
}