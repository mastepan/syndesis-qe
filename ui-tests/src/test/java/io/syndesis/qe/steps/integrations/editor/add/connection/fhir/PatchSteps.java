package io.syndesis.qe.steps.integrations.editor.add.connection.fhir;

import cucumber.api.java.en.Then;
import gherkin.ast.DataTable;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir.Create;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.fhir.Patch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatchSteps {

    private Patch patch = new Patch();

    @Then("^fill in JSON patch$")
    public void fillresourceType(DataTable table) {
        patch.fillJsonPatch(table.getRows().get(0).getCells().get(0).toString());
    }
}
