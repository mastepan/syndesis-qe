package io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir;

import org.openqa.selenium.By;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Search {

    private static final class Element {
        public static final By QUERY = By.id("TODO...   ");
    }

    public void fillInSearchQuery(String query) {
        log.debug("filling FHIR search query {}", query);
    //TODO
    }

}
