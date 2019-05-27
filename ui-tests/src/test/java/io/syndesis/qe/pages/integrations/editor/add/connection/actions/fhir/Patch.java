package io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import io.syndesis.qe.pages.SyndesisPage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Patch {

    private static final class Element {
        public static final By JSON_PATCH = By.id("jsonPatch");
    }

    public void fillJsonPatch(String json) {
        log.debug("filling FHIR patch JSON: {}", json);
    //TODO
    }

}
