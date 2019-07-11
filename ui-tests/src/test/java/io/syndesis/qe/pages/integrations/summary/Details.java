package io.syndesis.qe.pages.integrations.summary;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import io.syndesis.qe.CustomWebDriverProvider;
import io.syndesis.qe.pages.ModalDialogPage;
import io.syndesis.qe.pages.SyndesisPageObject;

import org.openqa.selenium.By;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.io.File;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Details extends SyndesisPageObject {

    ModalDialogPage modal = new ModalDialogPage();

    private static final class Element {
        public static final By ROOT = By.className("pf-c-page__main");
        public static final By STATUS = By.cssSelector("div[data-testid=\"syndesis-integration-status\"]");
        public static final By STARTING_STATUS = By.cssSelector("div[data-testid=\"progress-with-link-value\"]");
        public static final By PUBLISHED_VERSION = By.className("integration-detail-info__status");
        public static final By TITLE = By.className("pf-c-title.pf-m-lg.integration-detail-editable-name.pf-u-mr-lg");
        public static final By KEBEB_OPEN_MENU = By.className("dropdown-toggle");
        public static final By KEBAB_DROPDOWN_MENU = By.className("dropdown-menu-right");
        public static final By INFO = By.className("integration-detail__info");

        public static final By MULTIFLOW_COUNT = By.cssSelector("#multi-flow div.icon span.badge");

        public static final By TAB = By.cssSelector("ul.nav-tabs > li > a");
    }

    public static final class Status {
        public static final String ACTIVE = "Published";
        public static final String INACTIVE = "Unpublished";
        public static final String DRAFT = "Draft";
        public static final String IN_PROGRESS = "In Progress";
    }

    public static final class Actions {
        public static final String START = "Start Integration";
        public static final String STOP = "Stop Integration";
        public static final String DELETE = "Delete Integration";
        public static final String EDIT = "Edit Integration";
    }

    public SelenideElement getRootElement() {
        SelenideElement elementRoot = $(Element.ROOT).shouldBe(visible);
        return elementRoot;
    }

    @Getter
    private Activity activityTab = new Activity();

    @Getter
    private Metrics metricsTab = new Metrics();

    public boolean validate() {
        return getRootElement().is(visible);
    }

    public String getIntegrationName() {
        return this.getElementText(Element.TITLE);
    }

    public void deleteIntegration() {
        this.getButton(Actions.DELETE).shouldBe(visible).click();
        modal.getButton("OK").shouldBe(visible).click();
    }

    public void editIntegration() {
        this.getLink(Actions.EDIT).shouldBe(visible).click();
    }

    public void done() {
        this.getButton("Done").shouldBe(visible).click();
    }

    public void toggleIntegrationState() {
        String status = getStatus();

        if (status.equals(Status.DRAFT) || status.equals(Status.INACTIVE)) {
            this.getButton(Actions.START).shouldBe(visible).click();
            modal.getButton("OK").shouldBe(visible).click();
        } else if (status.equals(Status.ACTIVE)) {
            this.getButton(Actions.STOP).shouldBe(visible).click();
            modal.getButton("OK").shouldBe(visible).click();
        } else {
            log.error("Integration state {} cant be toggled!", status);
        }
    }

    public String getStatus() {
        return this.getElementText(Element.STATUS);
    }

    public String getStartingStatus() {
        return $(Element.STARTING_STATUS).shouldBe(visible).text();
    }

    public String getIntegrationInfo() {
        return this.getElementText(Element.INFO);
    }

    public SelenideElement getActionButton(String action) {
        return this.getButton(action);
    }

    public File exportIntegration() throws InterruptedException {

        this.getButton("Export").shouldBe(visible).click();

        String filePath = CustomWebDriverProvider.DOWNLOAD_DIR + File.separator + this.getIntegrationName() + "-export.zip";

        // wait for download
        Thread.sleep(5000);

        return new File(filePath);
    }

    public int getFlowCount() {
        return Integer.parseInt($(Element.MULTIFLOW_COUNT).shouldBe(visible).getText());
    }

    public void clickOnKebabMenuAction(String action) {
        // open kebab menu
        this.getRootElement().$(Element.KEBEB_OPEN_MENU).shouldBe(visible).click();

        // click on action
        this.getRootElement().$(Element.KEBAB_DROPDOWN_MENU).shouldBe(visible).$$(By.tagName("a"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(3)).findBy(exactText(action)).shouldBe(visible).click();
    }

    public void selectTab(String tabName) {
        ElementsCollection allTabs = $(Element.ROOT).findAll(Element.TAB).shouldBe(sizeGreaterThan(0));
        allTabs.stream().filter(s -> tabName.equals(s.getText())).findFirst().get().shouldBe(visible).click();
    }

    public SelenideElement getPublishedVersion() {
        return $(Element.PUBLISHED_VERSION);
    }

    public String getApiUrl() {
        return getRootElement()
            .findAll("input").shouldHaveSize(1).first().shouldBe(enabled, visible)
            .getValue();
    }
}
