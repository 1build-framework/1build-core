package dev.onebuild.testing.html.api;

import java.util.List;

public interface Document {
    boolean exists(String id);
    void click(String id);
    void setValue(String id, String value);
    String getValue(String id);
    MultiValueData getMultiValueData(String id);
    void takeScreenshot(String screenshotName);
    String getLogs();
    void printLogs();
}