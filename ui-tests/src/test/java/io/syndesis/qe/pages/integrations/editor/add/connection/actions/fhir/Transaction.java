package io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir;

import org.openqa.selenium.By;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Transaction {

    private static final class Element {
        public static final By INCLUDED_RESOURCES_TYPES = By.id("TODO...   ");
    }

    public void selectResources(List<String> resourceTypes) {
        log.debug("selecting FHIR resources: {}", resourceTypes.toString());
    //TODO
    }

}
