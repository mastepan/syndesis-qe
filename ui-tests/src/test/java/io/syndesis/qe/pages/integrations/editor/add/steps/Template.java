package io.syndesis.qe.pages.integrations.editor.add.steps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import java.io.File;

import io.syndesis.qe.pages.customizations.connectors.wizard.steps.UploadSwaggerSpecification;
import io.syndesis.qe.pages.integrations.editor.add.steps.getridof.AbstractStep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Template extends AbstractStep {

    private static final class Input {
        public static final By UPLOAD_FILE = By.id("");
        public static final By USE_URL = By.id("");
        public static final By CREATE = By.id("");
        public static final By URL = By.id("");
        public static final By EDITOR = By.id("");
    }

    private static final class Link {
        public static final By BROWSE = By.id("");
    }

    private static final class Button {
        public static final By CANCEL = By.id("");
        public static final By SAVE = By.id("");
        public static final By VALIDATE = By.id("");
    }

    private static final class Element {
        public static final By IMPORT_REVIEW = By.id("");
    }

    public Template() {
        super();
    }

    @Override
    public void fillConfiguration() {

    }

    public boolean validate() {
        return getRootElement().is(visible);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void setParameter(String parameter) {

    }

    @Override
    public String getParameter() {
        return null;
    }

    public void upload(String source, String urlOrContent) {
        switch (source) {
            case "file":
                uploadFileFromPath(urlOrContent);
                break;
            case "url":
                uploadFileFromUrl(urlOrContent);
                break;
            case "editor":
                createFileInEditor(urlOrContent);
                break;
            default:
                break;
        }
    }

    public void uploadFileFromPath(String url) {
        $(Input.UPLOAD_FILE).shouldBe(visible).click();
        $(Link.BROWSE).shouldBe(visible).click();
        $(Input.URL).shouldBe(visible).sendKeys(url);
        validateTemplate();
    }

    public void uploadFileFromUrl(String url) {
        $(Input.USE_URL).shouldBe(visible).click();
        $(Input.URL).shouldBe(visible).sendKeys(url);
        validateTemplate();
    }

    public void createFileInEditor(String content) {
        $(Input.USE_URL).shouldBe(visible).click();
        $(Input.URL).shouldBe(visible).sendKeys(content);
        validateTemplate();
    }

    public void validateTemplate() {
        $(Button.VALIDATE).shouldBe(visible).click();
    }
}
