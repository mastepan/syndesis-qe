package io.syndesis.qe.steps.integrations.editor.add.connection.fhir;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.syndesis.qe.pages.integrations.editor.add.connection.ChooseAction;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.database.InvokeSql;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.database.PeriodicSql;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir.Create;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.fragments.ConfigureAction;
import io.syndesis.qe.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FhirActionSteps {

    private Create create = new Create();

    @Then("^fill in resource type with \"([^\"]*)\" value$")
    public void fillresourceType(String type) {
        create.selectResourceType(type);
    }
}
