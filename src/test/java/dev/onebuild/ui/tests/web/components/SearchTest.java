package dev.onebuild.ui.tests.web.components;

import dev.onebuild.ui.tests.web.AbstractBrowserTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("search")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchTest extends AbstractBrowserTest {
  @Test
  public void whenSearchAppStarts_itLoadsTheHomePage() {

    assertTrue(app.exists("searchText"));
    assertTrue(app.exists("searchButton"));

    app.setValue("searchText","Hello, ");
    app.click("searchButton");

    assertEquals("Hello, World", app.getValue("searchText"));
  }
}