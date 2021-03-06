package io.syndesis.qe.steps.integrations;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.fabric8.kubernetes.client.utils.Utils;
import io.syndesis.qe.pages.ModalDialogPage;
import io.syndesis.qe.pages.integrations.ImportIntegrationPage;
import io.syndesis.qe.pages.integrations.detail.IntegrationDetailPage;
import io.syndesis.qe.pages.integrations.edit.IntegrationEditPage;
import io.syndesis.qe.pages.integrations.edit.IntegrationFlowViewComponent;
import io.syndesis.qe.pages.integrations.edit.steps.BasicFilterStepComponent;
import io.syndesis.qe.pages.integrations.edit.steps.DataMapperComponent;
import io.syndesis.qe.pages.integrations.edit.steps.StepComponent;
import io.syndesis.qe.pages.integrations.list.IntegrationsListComponent;
import io.syndesis.qe.pages.integrations.list.IntegrationsListPage;
import io.syndesis.qe.utils.TestUtils;
import io.syndesis.qe.wait.OpenShiftWaitUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by sveres on 11/15/17.
 */
@Slf4j
public class IntegrationSteps {

    private IntegrationEditPage editPage = new IntegrationEditPage();
    private IntegrationDetailPage detailPage = new IntegrationDetailPage();
    private IntegrationsListPage listPage = new IntegrationsListPage();
    private IntegrationsListComponent listComponent = new IntegrationsListComponent();
    private IntegrationFlowViewComponent flowViewComponent = new IntegrationFlowViewComponent();
    private DataMapperComponent dataMapper = new DataMapperComponent();
    private ImportIntegrationPage importIntegrationPage = new ImportIntegrationPage();

    @When("^she sets the integration name \"([^\"]*)\"$")
    public void setIntegrationName(String integrationName) {
        editPage.getIntegrationBasicsComponent().setName(integrationName);
    }

    @Then("^she is presented with a visual integration editor$")
    public void verifyEditorOpened() {
        editPage.getRootElement().shouldBe(visible);
        editPage.getIntegrationConnectionSelectComponent().getRootElement().shouldBe(visible);
        editPage.getFlowViewComponent().getRootElement().shouldBe(visible);
    }

    @Then("^she is presented with a visual integration editor for \"([^\"]*)\"$")
    public void verifyEditorOpenedFor(String integrationName) {
        this.verifyEditorOpened();
        log.info("editor must display integration name {}", integrationName);
        assertThat(editPage.getFlowViewComponent().getIntegrationName(), is(integrationName));
    }

    @Then("^Camilla is presented with \"([^\"]*)\" integration details$")
    public void verifyIntegrationDetails(String integrationName) {
        log.info("Integration detail editPage must show integration name");
        assertThat(detailPage.getIntegrationName(), is(integrationName));
    }

    @When("^Camilla selects the \"([^\"]*)\" integration.*$")
    public void selectIntegration(String itegrationName) {
        listPage.getListComponent().goToIntegrationDetail(itegrationName);
    }

    @When("^she selects \"([^\"]*)\" integration action$")
    public void selectIntegrationAction(String action) {
        if ("Create Opportunity".equals(action)) {
            log.warn("Action {} is not available", action);
            editPage.getListActionsComponent().selectAction("Create Salesforce object");
        }
        editPage.getListActionsComponent().selectAction(action);
    }

    @When("^Camilla deletes the \"([^\"]*)\" integration*$")
    public void deleteIntegration(String integrationName) {
        listPage.getListComponent().clickDeleteIntegration(integrationName);
    }

    @When("^Camilla deletes the integration on detail page*$")
    public void deleteIntegrationOnDetailPage() {
        detailPage.deleteIntegration();
    }

    @Then("^she is presented with \"([^\"]*)\" integration status on Integration Detail page$")
    public void checkStatusOnIntegrationDetail(String expectedStatus) {
        String status = detailPage.getIntegrationInfo();
        log.info("Status: {}", status);
        Assertions.assertThat(status.contains(expectedStatus)).isTrue();
    }

    @When("^she selects \"([^\"]*)\" integration step$")
    public void chooseStep(String stepName) {
        log.info("Adding {} step to integration", stepName);
        editPage.getIntegrationStepSelectComponent().chooseStep(stepName);
    }

    @Then("^Integration \"([^\"]*)\" is present in integrations list$")
    public void expectIntegrationPresent(String name) {
        log.info("Verifying integration {} is present", name);
        assertThat(listPage.getListComponent().isIntegrationPresent(name), is(true));
    }

    @Then("^Camilla can not see \"([^\"]*)\" integration anymore$")
    public void expectIntegrationNotPresent(String name) {
        log.info("Verifying if integration {} is present", name);
        assertThat(listPage.getListComponent().isIntegrationPresent(name), is(false));
    }

    @Then("^she waits until integration \"([^\"]*)\" gets into \"([^\"]*)\" state$")
    public void waitForIntegrationState(String integrationName, String integrationStatus) {
        SelenideElement integration = listPage.getListComponent().getIntegration(integrationName);
        assertTrue(TestUtils.waitForEvent(status -> status.equals(integrationStatus), () -> listPage.getListComponent().getIntegrationItemStatus(integration),
                TimeUnit.MINUTES, 5, TimeUnit.SECONDS, 5));
    }

    @Then("^she is presented with Choose Step page$")
    public void verifyChooseStepPage() {
        log.info("there must be add step page root element");
        editPage.getIntegrationStepSelectComponent().getRootElement().shouldBe(visible);
    }

    @Then("^she is presented with \"([^\"]*)\" step configuration page$")
    public void verifyConfigureStepPage(String stepType) {
        StepComponent stepComponent = editPage.getStepComponent(stepType, "");
        log.info("there must be add step editPage root element");
        stepComponent.getRootElement().shouldBe(visible);
        assertThat(stepComponent.validate(), is(true));
    }

    @Then("^she fills the configuration page for \"([^\"]*)\" step with \"([^\"]*)\" parameter$")
    public void fillStepConfiguration(String stepType, String parameter) {
        StepComponent stepComponent = editPage.getStepComponent(stepType, parameter);
        stepComponent.fillConfiguration();
    }

    @Then("^she checks that basic filter step path input options contains \"([^\"]*)\" option$")
    public void checkBasicFilterStepOption(String option) {
        BasicFilterStepComponent basicFilterStepComponent = new BasicFilterStepComponent("");
        List<String> options = basicFilterStepComponent.getPathInputOptions();

        Assertions.assertThat(options.contains(option)).isTrue();
    }

    @Then("^she adds \"(\\d+)\" random steps and then checks the structure$")
    public void addRandomStepsAndCheckRest(Integer numberOfSteps) {
        log.info("Adding random steps");
        List<String> list = editPage.getFlowViewComponent().getStepsArray();
        editPage.getButton("Add a Step").shouldBe(visible).click();
        ElementsCollection links = editPage.getLinks("Add a step");
        Integer count = links.size();
        List<Integer> randomIndexes = new ArrayList<>();
        for (int i = 0; i < numberOfSteps; i++) {
            randomIndexes.add((int) Math.floor((Math.random() * count)));
        }
        for (int randomIndex : randomIndexes) {
            links.get(randomIndex).click();
            String stepType = "Basic Filter";
            String stepParameter = "ANY of the following, pathx " + randomIndex + ", Contains, valuex " + randomIndex;
            editPage.getIntegrationStepSelectComponent().chooseStep(stepType);
            StepComponent stepComponent = editPage.getStepComponent(stepType, stepParameter);
            stepComponent.fillConfiguration();
            editPage.getButton("Next").shouldBe(visible).click();
            editPage.getButton("Add a Step").shouldBe(visible).click();
            list.add(randomIndex, stepParameter);
        }
        List<String> list2 = editPage.getFlowViewComponent().getStepsArray();
        for (int i = 0; i < list2.size(); i++) {
            log.info("assserting {} and {}", list.get(i), list2.get(i));
            assertThat(list.get(i), is(list2.get(i)));
        }
    }

    //what rest??
    @Then("^she deletes \"(\\d+)\" random integration steps and checks the rest$")
    public void deleteRandomStepsAndCheckRest(Integer numberOfSteps) {
        log.info("Deleting random steps");
        List<String> list = editPage.getFlowViewComponent().getStepsArray();
        ElementsCollection deletes = editPage.getFlowViewComponent().getAllTrashes().shouldBe(sizeGreaterThanOrEqual(1));
        int count = deletes.size();
        List<Integer> randomIndexes = new ArrayList<>();
        for (int i = 0; i < numberOfSteps; i++) {
            randomIndexes.add((int) Math.floor(Math.random() * (count - 2 - i)));
        }
        for (Integer randomIndex : randomIndexes) {
            deletes.get(randomIndex + 1).click();
            editPage.getFirstVisibleButton("OK").shouldBe(visible).click();
            list.remove(randomIndex);
        }
        List<String> list2 = editPage.getFlowViewComponent().getStepsArray();
        for (int i = 0; i < list.size(); i++) {
            log.info("assserting {} and {", list.get(i), list2.get(i));
            assertThat(list.get(i), is(list2.get(i)));
        }
    }

    //what rest???
    @Then("^she deletes step on position \"(\\d+)\" and checks the rest$")
    public void deleteStepOnPositionAndCheckRest(Integer positionOfStep) {
        log.info("Deleting step on position {}", positionOfStep);
        List<String> list = editPage.getFlowViewComponent().getStepsArray();
        ElementsCollection deletes = this.editPage.getFlowViewComponent().getAllTrashes().shouldBe(sizeGreaterThanOrEqual(1));
        Integer indexOfStep = positionOfStep + 1;
        deletes.get(indexOfStep).click();
        editPage.getFirstVisibleButton("OK");
        list.remove(positionOfStep);
        //NOW CHECK:
        List<String> list2 = editPage.getFlowViewComponent().getStepsArray();
        for (int i = 0; i < list.size(); i++) {
            log.info("assserting {} and {}", list.get(i), list2.get(i));
            assertThat(list.get(i), is(list2.get(i)));
        }
    }

    @Then("^she is presented with an actions list$")
    public void verifyActionsList() {
        log.info("There must be action list loaded");
        editPage.getListActionsComponent().getRootElement().shouldBe(visible);
    }

    @Then("^add new basic filter rule with \"([^\"]*)\" parameters$")
    public void addBasicFilterRule(String rule) {
        BasicFilterStepComponent basicFilterStepPage = (BasicFilterStepComponent) editPage.getStepComponent("BASIC FILTER", "");
        basicFilterStepPage.initialize();
        basicFilterStepPage.addRule(rule);
    }

    @Then("^delete \"(\\d+)\" random basic filter rule$")
    public void deleteRandomFilterRules(Integer numberOfRules) {
        for (int i = 0; i < numberOfRules; i++) {
            editPage.getFlowViewComponent().clickRandomTrash();
        }
    }

    @Then("^delete basic filter rule on position \"(\\d+)\"$")
    public void deleteFilterRuleOnPosition(Integer position) {
        ElementsCollection trashes = editPage.getFlowViewComponent().getAllTrashes();
        trashes.get(position - 1).click();
    }

    //Kebab menu test, #553 -> part #548, #549.
    @When("^clicks on the kebab menu icon of each available Integration and checks whether menu is visible and has appropriate actions$")
    public void clickOnAllKebabMenus() {
        listPage.getListComponent().checkAllIntegrationsKebabButtons();
    }

    // Twitter search specification
    @Then("^she fills keywords field with random text to configure search action$")
    public void fillKeywords() {
        String value = Utils.randomString(20);
        editPage.getTwitterSearchComponent().fillInput(value);
    }

    @Then("^she fills \"(\\w+)\" action configure component input with \"([^\"]*)\" value$")
    public void fillActionConfigureField(String fieldId, String value) {
        log.info("Input should be visible");
        editPage.getActionConfigureComponent().fillInput(fieldId, value);
    }

    @Then("^she fills periodic query input with \"([^\"]*)\" value$")
    public void fillPerodicSQLquery(String query) {
        editPage.getPeriodicSqlComponent().fillSqlInput(query);
    }

    @Then("^she fills period input with \"([^\"]*)\" value$")
    public void fillSQLperiod(String period) {
        editPage.getPeriodicSqlComponent().fillSQLperiod(period);
    }

    @Then("^she fills invoke query input with \"([^\"]*)\" value$")
    public void fillInvokeSQLquery(String query) {
        editPage.getInvokeSqlComponent().fillSqlInput(query);
    }


    /**
     * whether it's start or finish connection
     *
     * @param position
     */
    @Then("^she is prompted to select a \"([^\"]*)\" connection from a list of available connections$")
    public void verifyTypeOfConnection(String position) {
        log.info("{} connection must be active", position);
        assertTrue("There was no active icon found for position " + position, editPage.getFlowViewComponent().verifyActivePosition(position));
    }

    @When("^she adds first step between START and STEP connection$")
    public void sheAddsFirstStep() throws Throwable {
        editPage.getFlowViewComponent().clickAddStepLink(0);
    }

    @When("^she adds second step between STEP and FINISH connection$")
    public void sheAddsSecond() throws Throwable {
        editPage.getFlowViewComponent().clickAddStepLink(1);
    }

    @And("^sets jms subscribe inputs source data$")
    public void setJmsSubscribeData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getJmsSubscribeComponent().checkAndGetFieldTypeById(field);
                assertThat(element, notNullValue());
                editPage.getJmsSubscribeComponent().setElementValue(source.get(field), element);
            }
        }
    }

    @And("^sets jms request inputs source data$")
    public void setJmsRequestData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getJmsSubscribeComponent().checkAndGetFieldTypeById(field);
                assertThat(element, notNullValue());
                editPage.getJmsSubscribeComponent().setElementValue(source.get(field), element);
            }
        }
    }

    @And("^sets jms publish inputs source data$")
    public void setJmsPublishData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getJmsPublishComponent().checkAndGetFieldTypeById(field);
                assertThat(element, notNullValue());
                editPage.getJmsPublishComponent().setElementValue(source.get(field), element);
            }
        }
    }

    @And("^she fills ftp download form with values$")
    public void setFtpDownloadData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getFtpDownloadComponent().checkAndGetFieldTypeByName(field);
                assertThat(element, notNullValue());
                editPage.getFtpDownloadComponent().setElementValue(source.get(field), element);
            }
        }
    }
    @And("^she fills specify output data type form with values$")
    public void setOutputDataTypeData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getFtpDatatypeComponent().checkAndGetFieldTypeByName(field);
                assertThat(element, notNullValue());
                editPage.getFtpDatatypeComponent().setElementValue(source.get(field), element);
            }
        }
    }

    @And("^she fills ftp upload form with values$")
    public void setFtpUploadData(DataTable sourceMappingData) {
        for (Map<String, String> source : sourceMappingData.asMaps(String.class, String.class)) {
            for (String field : source.keySet()) {
                SelenideElement element = editPage.getFtpUploadComponent().checkAndGetFieldTypeByName(field);
                assertThat(element, notNullValue());
                editPage.getFtpUploadComponent().setElementValue(source.get(field), element);
            }
        }
    }

    @And("^Camilla exports this integraion$")
    public void exportIntegration() throws InterruptedException {
        File exportedIntegrationFile = detailPage.exportIntegration();
        Assertions.assertThat(exportedIntegrationFile)
                .exists()
                .isFile()
                .has(new Condition<>(f -> f.length() > 0, "File size should be greater than 0"));
    }

    @And("^Camilla imports integraion \"([^\"]*)\"$")
    public void importIntegration(String integrationName) throws InterruptedException {
        importIntegrationPage.importIntegration(integrationName);
        Assertions.assertThat(listComponent.isIntegrationPresent(integrationName)).isTrue();
    }

    @And("^Camilla starts integration \"([^\"]*)\"$")
    public void startIntegration(String integrationName) {
        detailPage.clickOnKebabMenuAction("Publish");
        ModalDialogPage modal = new ModalDialogPage();
        modal.getButton("OK").shouldBe(visible).click();
    }

    @And("^Wait until there is no integration pod with name \"([^\"]*)\"$")
    public void waitForIntegrationPodShutdown(String integartionPodName) throws InterruptedException {
        OpenShiftWaitUtils.assertEventually("Pod with name " + integartionPodName + "is still running.",
                OpenShiftWaitUtils.areNoPodsPresent(integartionPodName), 1000, 5 * 60 * 1000);
    }

    @And("^Camilla drags exported integration \"([^\"]*)\" file to drag and drop area$")
    public void importIntegrationViaDragAndDrop(String integrationName) throws InterruptedException {
        importIntegrationPage.importIntegrationViaDragAndDrop(integrationName);
        Assertions.assertThat(listComponent.isIntegrationPresent(integrationName)).isTrue();
    }

    @Then("^she selects \"([^\"]*)\" from sql dropdown$")
    public void selectsFromDopdownByClassName(String timeUnits) {
        editPage.getPeriodicSqlComponent().selectSQLperiodUnits(timeUnits);
    }

    @And("^.*checks? that text \"([^\"]*)\" is \"([^\"]*)\" in hover table over \"([^\"]*)\" step$")
    public void checkTextInHoverTable(String text, String isVisible, String stepPosition) throws InterruptedException {
        if (isVisible.equalsIgnoreCase("visible")) {
            Assertions.assertThat(flowViewComponent.checkTextInHoverTable(stepPosition))
                    .isNotEmpty()
                    .containsIgnoringCase(text);
        } else {
            Assertions.assertThat(flowViewComponent.checkTextInHoverTable(stepPosition))
                    .isNotEmpty()
                    .doesNotContain(text);
        }
    }

    @And("^.*checks? that data bucket \"([^\"]*)\" is available and opens? it$")
    public void checkPreviousDataBuckets(String bucket) {
        dataMapper.openBucket(By.id(bucket));
    }

    /**
     * Every step element has class step and every option to add step/connection also has class step.
     * So if you have 3 steps created, stepPosition is: first = 0, second = 2, third = 4 etc.
     *
     * @param text
     * @param isVisible
     * @param stepPosition
     * @throws InterruptedException
     */
    @And("^.*checks? that text \"([^\"]*)\" is \"([^\"]*)\" in step warning inside of step number \"([^\"]*)\"")
    public void checkTextInStepWarning(String text, String isVisible, int stepPosition) throws InterruptedException {
        if (isVisible.equalsIgnoreCase("visible")) {
            Assertions.assertThat(flowViewComponent.getWarningTextFromStep(stepPosition))
                    .isNotEmpty()
                    .containsIgnoringCase(text);
        } else {
            Assertions.assertThat(flowViewComponent.getWarningTextFromStep(stepPosition))
                    .isNotEmpty()
                    .doesNotContain(text);
        }
    }

    @And("^.*checks? that in connection info popover for step number \"([^\"]*)\" is following text$")
    public void checkTextInConnectionInfo(int stepPosition, DataTable connectionsData) throws InterruptedException {

        List<String> data = connectionsData.asList(String.class);
        String foundText = flowViewComponent.getConnectionPropertiesText(flowViewComponent.getStepOnPosition(stepPosition));

        Assertions.assertThat(foundText).isNotEmpty();

        for (String column : data) {
            Assertions.assertThat(foundText)
                    .containsIgnoringCase(column);
        }
    }

    /**
     * Every step element has class step and every option to add step/connection also has class step.
     * If you have 3 steps created, position is: first = 0, second = 2, third = 4 etc.
     *
     * @param position index of element with class .step
     */
    @And(".*checks? that there is no warning inside of step number \"([^\"]*)\"$")
    public void checkIfWarningIsVisible(int position) {
        Assertions.assertThat(flowViewComponent.getStepWarningElement(position).isDisplayed()).isFalse();
    }
}
