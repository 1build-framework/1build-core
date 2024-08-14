package dev.onebuild.ui.tests.domain.service;

import dev.onebuild.ui.domain.service.ScriptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
public class ScriptServiceTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScriptServiceTest.class);

  @Autowired
  private ScriptService scriptService;

  @Test
  public void whenIndexIsCalled_itGeneratesThePage() {
    String page = scriptService.renderIndex();
    assertNotNull(page);
    log.info("Page generated " + page);
    assertTrue(page.contains("<!DOCTYPE html>"));
    assertTrue(page.contains("<html lang=\"en\">"));
    assertTrue(page.contains("<head>"));

    //css
    assertTrue(page.contains("/public/css/materialdesignicons.css"));
    assertTrue(page.contains("/libs/css/vuetify-3.5.2.min.css"));
    assertTrue(page.contains("/libs/css/onebuild.css"));

    //js
    assertTrue(page.contains("/libs/js/vue-3.4.15.global.prod.js"));
    assertTrue(page.contains("/libs/js/vue-router-4.2.5.js"));
    assertTrue(page.contains("/libs/js/vuetify-3.5.2.min.js"));
    assertTrue(page.contains("/libs/js/vue-3.4.19.esm-browser.js"));
    assertTrue(page.contains("/libs/js/onebuild.js"));

    //Import
    assertTrue(page.contains("/app/components/About"));
  }

  @Test
  public void whenComponentIsCalled_itGeneratesTheScript() {
    String componentScript = scriptService.renderComponent("App");
    assertNotNull(componentScript);
    log.info("Page generated \n" + componentScript);

    assertTrue(componentScript.contains("use strict"));
    assertTrue(componentScript.contains("const { ref } = Vue;"));
    assertTrue(componentScript.contains("name: 'App'"));
    assertTrue(componentScript.contains("setup"));
    assertTrue(componentScript.contains("template:"));
  }

}
