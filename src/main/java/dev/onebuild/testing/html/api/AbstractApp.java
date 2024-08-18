package dev.onebuild.testing.html.api;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

@Slf4j
public abstract class AbstractApp implements Document {

  protected final WebDriver driver;

  public AbstractApp(WebDriver driver) {
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

  public void waitSeconds(int seconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='fake-id-for-wait']")));
    } catch(Exception e) {
      log.info("{} seconds wait over", seconds);
    }
  }

  public Map<String, Object> getElementAttributes(WebDriver driver, WebElement element) {
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
    log.info("Element: Tag = {}, Id = {}, Text = {}", element.getTagName(), element.getAttribute("id"), element.getText());
  }


}
