package dev.onebuild.testing.html.api;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
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

@Slf4j
public abstract class AbstractApp implements BrowserApp {

  protected final WebDriver driver;

  protected final String debugFolder;

  public AbstractApp(String debugFolder, WebDriver driver) {
    this.debugFolder = debugFolder;
    this.driver = driver;
  }

  @Override
  public boolean exists(String id) {
    return !driver.findElements(By.id(id)).isEmpty();
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
  public void setInputValue(String id, String value) {
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
  public String getInputValue(String id) {
    takeScreenshot("BEFORE_GET_" + id);

    WebElement element = driver.findElement(By.id(id));
    if(element != null) {
      return element.getAttribute("value");
    } else {
      log.warn("Element with id {} not found", id);
      return "";
    }
  }

  public void waitSeconds(int seconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='fake-id-for-wait']")));
    } catch(Exception e) {
      log.info("{} seconds wait over", seconds);
    }
  }

  public WebElement waitUntil(int seconds, By by) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
      return wait.until(ExpectedConditions
          .presenceOfElementLocated(by)
      );
    } catch(Exception e) {
      log.info("{} seconds wait over", seconds);
    }
    return null;
  }

  /*public Map<String, Object> getElementAttributes(WebDriver driver, WebElement element) {
    String script = """
        let attrs = {};
        for (let attr of arguments[0].attributes) {
            attrs[attr.name] = attr.value;
        }
        return attrs;
        """;
    JavascriptExecutor js = (JavascriptExecutor) driver;
    return (Map<String, Object>) js.executeScript(script, element);
  }

  public void printElementDetails(WebElement element) {

    log.info("Element: Class {}, Tag = {}, id = {}, name = {}, class = {}, text = {}",
        element.getClass(),
        element.getTagName(),
        element.getAttribute("id"),
        element.getAttribute("name"),
        element.getAttribute("class"),
        element.getText());
  }*/

  @Override
  public void savePageSource() {
    if(debugFolder == null) {
      return;
    }

    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
    String source = (String) jsExecutor.executeScript("return document.documentElement.outerHTML;");

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
      File destinationFile = getDestinationFile(screenshotName + ".png");
      if(destinationFile != null) {
        Files.copy(screenshot.toPath(), destinationFile.toPath());
      }
    } catch (IOException e) {
      log.debug("Failed to take screenshot", e);
    }
  }

  @Override
  public final String getLogs() {
    StringBuilder logs = new StringBuilder();
    addLogs(LogType.BROWSER, logs);
    addLogs(LogType.CLIENT, logs);
    addLogs(LogType.DRIVER, logs);
    return logs.toString();
  }

  @Override
  public void printLogs() {
    log.info("\n<-------------------- Browser Logs -------------------->\n");
    log.info(getLogs());
    log.info("\n<-------------------- Browser Logs -------------------->\n");
  }

  private File getDestinationFile(String fileName) {
    Path directoryPath = Paths.get(debugFolder);

    try {
      if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
      }
      return new File(directoryPath.toFile(), System.currentTimeMillis() + "-" + fileName);
    } catch (IOException e) {
      log.error("Failed to create directory: {}", directoryPath, e);
      return null;
    }
  }

  private void addLogs(String logType, StringBuilder logs) {
    LogEntries logEntries = driver.manage().logs().get(logType);
    for (LogEntry entry : logEntries) {
      logs.append(entry.getMessage()).append("\n");
    }
  }


}
