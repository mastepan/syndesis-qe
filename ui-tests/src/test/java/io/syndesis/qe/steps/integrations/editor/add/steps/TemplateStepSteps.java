package io.syndesis.qe.steps.integrations.editor.add.steps;

import static org.junit.Assert.assertThat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.Map;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import io.fabric8.kubernetes.client.utils.Utils;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.database.PeriodicSql;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.ftp.FtpDataType;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.ftp.FtpDownload;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.ftp.FtpUpload;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.jms.JmsPublish;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.jms.JmsSubscribe;
import io.syndesis.qe.pages.integrations.editor.add.connection.actions.twitter.TwitterSearch;
import io.syndesis.qe.pages.integrations.editor.add.steps.BasicFilter;
import io.syndesis.qe.pages.integrations.editor.add.steps.getridof.AbstractStep;
import io.syndesis.qe.pages.integrations.editor.add.steps.getridof.StepFactory;
import io.syndesis.qe.pages.integrations.fragments.IntegrationFlowView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateStepSteps {

    //upload template
	//[datatable]
	// source | data
}
