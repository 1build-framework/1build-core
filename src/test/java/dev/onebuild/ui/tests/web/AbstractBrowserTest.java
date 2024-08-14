package dev.onebuild.ui.tests.web;

import dev.onebuild.testing.html.vuetify.OneBuildApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;


public abstract class AbstractBrowserTest {

  @LocalServerPort
  private int port;

  private BrowserWebDriverContainer<?> browserContainer;

  private WebDriver driver;

  protected OneBuildApp app;

  @BeforeEach
  public void setUp() {
    beforeEach();
    var chromeOptions = new ChromeOptions();
    chromeOptions.setCapability("goog:loggingPrefs", Map.of(LogType.BROWSER, "ALL"));

    browserContainer = new BrowserWebDriverContainer<>(DockerImageName.parse("selenium/standalone-chrome:latest"))
        .withCapabilities(chromeOptions);
    browserContainer.start();
    driver = browserContainer.getWebDriver();

    app = new OneBuildApp(
        "http://host.docker.internal:" + getPort() + getHomePageUrl(),
        getDebugFolder(),
        driver);
  }

  protected void beforeEach() {

  }

  protected void afterEach() {

  }

  @AfterEach
  public void tearDown() {
    afterEach();

    if (browserContainer != null) {
      browserContainer.stop();
    }
  }

  protected String getDebugFolder() {
    return null;
  }

  protected String getHomePageUrl() {
    return "/index.html";
  }

  protected int getPort() {
    return port;
  }

}
