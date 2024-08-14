package dev.onebuild.testing.html.vuetify;

import dev.onebuild.testing.html.api.Document;
import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.testing.html.api.MultiValueData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OneBuildApp implements Document {
  private final WebDriver driver;
  private String debugFolder;

  public OneBuildApp(String url, String debugFolder, WebDriver driver) {
    this.driver = driver;
    this.debugFolder = debugFolder;
    create(url);
  }

  private void create(String url) {
    driver.get(url);

//    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchButton")));

    log.debug("Current URL: " + driver.getCurrentUrl());

    takeScreenshot("READY");
    savePageSource();
  }

  @Override
  public boolean exists(String id) {
    return driver.findElements(By.id(id)).size() > 0;
  }

  @Override
  public void click(String id) {
    takeScreenshot("BEFORE_CLICK_" + id);

    WebElement element = driver.findElement(By.id(id));
    if(element != null) {
      element.click();
      takeScreenshot("AFTER_CLICK_" + id);
    } else {
      log.warn("Element with id {} not found", id);
    }
  }

  @Override
  public void setValue(String id, String value) {
    takeScreenshot("BEFORE_SET_" + id);

    WebElement element = driver.findElement(By.id(id));
    if(element != null) {
      element.sendKeys(value);
      takeScreenshot("AFTER_SET_" + id);
    } else {
      log.warn("Element with id {} not found", id);
    }
  }

  @Override
  public String getValue(String id) {
    takeScreenshot("BEFORE_GET_" + id);

    WebElement element = driver.findElement(By.id(id));
    if(element != null) {
      return element.getAttribute("value");
    } else {
      log.warn("Element with id {} not found", id);
      return "";
    }
  }

  @Override
  public MultiValueData getMultiValueData(String id) {
    List<IdValue> values = null;
    List<IdValue> selectedValues = null;

    //String xpathExpression = "//div[@data-test-id='" + id + "']//ancestor::div[contains(@class, 'v-select')]";
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-test-id='" + id + "']")));
    takeScreenshot("BEFORE_CLICK_" + id);
    Actions actions = new Actions(driver);
    actions.moveToElement(selectElement).click().perform();

    List<Map<String, String>> options = getAllOptions();

    log.info("Options: {}", options);

//    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'v-list-item__title')]")));
    takeScreenshot("AFTER_CLICK_" + id);

    log.info("Select element: {}", selectElement);

//    values = map(optionElements);
    selectElement.click();

    return MultiValueData.builder()
        .values(values)
        .selectedValues(selectedValues)
        .build();
  }

  private List<Map<String, String>> getAllOptions() {
    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

    // Execute the JavaScript and get the options
    List<Map<String, String>> options = (List<Map<String, String>>) jsExecutor.executeScript(
        String.format("""
            function getVuetifySelectOptions(selectName) {
                const vSelect = document.querySelector(`[name=\"%s\"]`);
                if (!vSelect) return [];
                vSelect.click();
                const options = [];
                document.querySelectorAll('.v-list-item__title').forEach(function(option) {
                    options.push({
                        value: option.textContent.trim()
                    });
                });
                vSelect.click();
                return options;
            }
            return getVuetifySelectOptions(\"%s\");
            """, "databaseTypes", "databaseTypes")
    );

    // Print the options
    for (Map<String, String> option : options) {
      System.out.println(option.get("value"));
    }

    return options;
  }

  private void waitSeconds(int seconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
      wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'v-list-item__title')]")));
//      wait.wait();
    } catch(Exception e) {
      log.error("Failed to wait for {} seconds", seconds, e);
    }
  }

  private List<IdValue> map(List<WebElement> optionElements) {
    List<IdValue> values = new ArrayList<>();
    for (WebElement optionElement : optionElements) {
      String optionId = optionElement.getAttribute("value");
      String optionLabel = optionElement.getText();
      values.add(new IdValue(optionId, optionLabel));
    }
    return values;
  }

  public void savePageSource() {
    if(debugFolder == null) {
      return;
    }

//    String source = driver.getPageSource();
    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
    String source = (String) jsExecutor.executeScript("return document.documentElement.outerHTML;");

//    log.info("Page Source: {}", source);

    try (FileWriter writer = new FileWriter(getDestinationFile("page-source.html"))) {
      writer.write(source);
    } catch (IOException e) {
      log.error("Failed to save page source", e);
    }
  }

  @Override
  public void takeScreenshot(String screenshotName) {
    if(debugFolder == null) {
      return;
    }

    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    try {
      // Define a path within the container
      File destinationFile = getDestinationFile(screenshotName + ".png");
      if(destinationFile != null) {
        Files.copy(screenshot.toPath(), destinationFile.toPath());
      }
    } catch (IOException e) {
      log.debug("Failed to take screenshot", e);
    }
  }

  private File getDestinationFile(String fileName) {
    Path directoryPath = Paths.get(debugFolder);

    try {
      if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
      }

      File file = new File(directoryPath.toFile(), System.currentTimeMillis() + "-" + fileName);

      return file;
    } catch (IOException e) {
      log.error("Failed to create directory: {}", directoryPath, e);
      return null;
    }
  }

  @Override
  public String getLogs() {
    StringBuilder logs = new StringBuilder();
    addLogs(LogType.BROWSER, logs);
    addLogs(LogType.CLIENT, logs);
    addLogs(LogType.DRIVER, logs);
    return logs.toString();
  }

  private void addLogs(String logType, StringBuilder logs) {
    LogEntries logEntries = driver.manage().logs().get(logType);
    for (LogEntry entry : logEntries) {
      logs.append(entry.getMessage()).append("\n");
    }
  }

  @Override
  public void printLogs() {
    log.info("\n<-------------------- Browser Logs -------------------->\n");
    log.info(getLogs());
    log.info("\n<-------------------- Browser Logs -------------------->\n");
  }
}
