package dev.onebuild.testing.html.vuetify;

import dev.onebuild.testing.html.api.AbstractApp;
import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.testing.html.api.MultiValueData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Vuetify370App extends AbstractApp {

  public Vuetify370App(String url, String debugFolder, WebDriver driver) {
    super(debugFolder, driver);
    create(url);
  }

  @Override
  public MultiValueData getMultiValueData(String id) {
    WebElement parentElement = waitUntil(1, By.xpath("//*[@id='" + id + "']/ancestor::*[2]"));

    //Perform click to render all options
    takeScreenshot("BEFORE_CLICK_" + id);
    parentElement.click();

    //Wait for it to render
    waitSeconds(1);
    takeScreenshot("AFTER_CLICK_" + id);

    //All Options
    List<WebElement> optionElements = parentElement
        .findElements(
            By.xpath("//*[@class='v-list-item__content']")
        );

    //Selected Option
    WebElement selectElement = waitUntil(1, By.xpath("//*[@id='" + id + "']"));
    String value = selectElement.getAttribute("value");

    List<IdValue> selectedValues;
    if(value == null) {
      selectedValues = Collections.emptyList();
    } else {
      selectedValues = List.of(new IdValue(value, value));
    }

    return MultiValueData.builder()
        .values(map(optionElements))
        .selectedValues(selectedValues)
        .build();
  }

  private List<IdValue> map(List<WebElement> optionElements) {
    List<IdValue> values = new ArrayList<>();
    for (WebElement optionElement : optionElements) {
      String optionId = optionElement.getText();
      String optionLabel = optionElement.getText();
      values.add(new IdValue(optionId, optionLabel));
    }
    return values;
  }

  @Override
  public void setSelectValue(String id, String value) {
    WebElement selectElement = waitUntil(1, By.xpath("//*[@id='" + id + "']/ancestor::*[2]"));
    selectElement.click();
    waitSeconds(1);

    List<WebElement> optionElements = selectElement
        .findElements(
            By.xpath("//*[@class='v-list-item__content']")
        );

    optionElements.stream()
        .filter(optionElement -> optionElement.getText().equals(value))
        .findFirst()
        .ifPresent(WebElement::click);
  }

}