package dev.onebuild.ui.tests.domain.service;

import dev.onebuild.commons.domain.model.OneBuildAppSettings;
import dev.onebuild.core.domain.service.ScriptService;
import dev.onebuild.ui.tests.config.UiTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import( {
    OneBuildAppSettings.class,
    UiTestConfiguration.class
})
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
    assertTrue(page.contains("/libs/css/vuetify-css-3.7.0.css"));

    //js
    assertTrue(page.contains("/libs/js/vue-3.4.38.js"));
    assertTrue(page.contains("/libs/js/vue-router-4.4.3.js"));
    assertTrue(page.contains("/libs/js/vuetify-3.7.0.js"));
    assertTrue(page.contains("/libs/js/vuedemi-2.2.2.js"));
    assertTrue(page.contains("/libs/js/pinia-2.2.2.js"));
    assertTrue(page.contains("/libs/js/axios-1.7.7.js"));

    //Import
    assertTrue(page.contains("/app/components/about"));

    //Scripts
    assertTrue(page.contains("Vue.createApp(App)"));
    assertTrue(page.contains("Pinia.createPinia()"));
    assertTrue(page.contains("Vuetify.createVuetify()"));
    assertTrue(page.contains("app.use(pinia)"));
    assertTrue(page.contains("app.use(vuetify)"));
    assertTrue(page.contains("app.mount('#onebuild-app')"));
  }

  @Test
  public void whenComponentIsCalled_itGeneratesTheScript() {
    String componentScript = scriptService.renderScript("/app/components", "app");
    assertNotNull(componentScript);
    log.info("Page generated \n" + componentScript);

    assertTrue(componentScript.contains("use strict"));
    assertTrue(componentScript.contains("const { ref } = Vue;"));
    assertTrue(componentScript.contains("name: 'App'"));
    assertTrue(componentScript.contains("setup"));
    assertTrue(componentScript.contains("template:"));
  }

}
