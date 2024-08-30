package dev.onebuild.ui.tests.web;

import dev.onebuild.testing.html.vuetify.Vuetify370App;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public abstract class AbstractComponentTest {
  private BrowserWebDriverContainer<?> browserContainer;
  protected Vuetify370App app;

  protected void openBrowser() {
    var chromeOptions = new ChromeOptions();
    chromeOptions.setCapability("goog:loggingPrefs", Map.of(LogType.BROWSER, "ALL"));

    browserContainer = new BrowserWebDriverContainer<>(DockerImageName.parse("selenium/standalone-chrome:latest"))
        .withCapabilities(chromeOptions);
    browserContainer.start();
    WebDriver driver = browserContainer.getWebDriver();

    app = new Vuetify370App(
        "http://host.docker.internal:" + getPort() + getHomePageUrl(),
        getDebugFolder(),
        driver);
  }

  protected void closeBrowser() {
    if (browserContainer != null) {
      browserContainer.stop();
    }
  }

  //If provided, it takes screenshots and saves the page at this location.
  protected abstract String getDebugFolder();

  //Home page where component is rendered
  protected abstract String getHomePageUrl();

  //Port the server is running on
  protected abstract int getPort();

}