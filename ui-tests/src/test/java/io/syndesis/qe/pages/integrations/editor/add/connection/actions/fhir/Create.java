package io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import io.syndesis.qe.pages.integrations.editor.add.connection.actions.database.Sql;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Create {

    private static final class Element {
        public static final By RESOURCE_TYPE = By.cssSelector("select[data-testid='resourcetype']");
    }

    public void selectResourceType(String resourceType) {
        log.debug("selecting FHIR resource type: {}", resourceType);
        SelenideElement selectElement = $(Element.RESOURCE_TYPE).shouldBe(visible);
        selectElement.selectOption(resourceType);
    }

}
