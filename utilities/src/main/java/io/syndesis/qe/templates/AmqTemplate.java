package io.syndesis.qe.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.openshift.api.model.Template;
import io.syndesis.qe.accounts.Account;
import io.syndesis.qe.accounts.AccountsDirectory;
import io.syndesis.qe.utils.OpenShiftUtils;
import io.syndesis.qe.wait.OpenShiftWaitUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmqTemplate {

    public static void deploy() {
        Template template = null;
        try (InputStream is = ClassLoader.getSystemResourceAsStream("templates/syndesis-amq.yml")) {
            template = OpenShiftUtils.client().templates().load(is).get();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read template ", ex);
        }
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("MQ_USERNAME", "amq");
        templateParams.put("MQ_PASSWORD", "topSecret");

        // try to clean previous broker
        cleanUp();
        OpenShiftUtils.client().templates().withName("syndesis-amq").delete();

        KubernetesList processedTemplate = OpenShiftUtils.getInstance().recreateAndProcessTemplate(template, templateParams);
        OpenShiftUtils.getInstance().createResources(processedTemplate);

        try {
            OpenShiftWaitUtils.waitFor(OpenShiftWaitUtils.isAPodReady("application", "broker"));
        } catch (InterruptedException | TimeoutException e) {
            log.error("Wait for syndesis-server failed ", e);
        }
        Account amqAccount = new Account();
        amqAccount.setService("amq");
        Map<String, String> accountParameters = new HashMap<>();
        accountParameters.put("brokerUrl", "tcp://broker-amq:61616");
        accountParameters.put("username", "amq");
        accountParameters.put("password", "topSecret");
        accountParameters.put("clientId", UUID.randomUUID().toString());
        amqAccount.setProperties(accountParameters);
        AccountsDirectory.getInstance().addAccount("AMQ", amqAccount);
    }

    private static void cleanUp() {
        OpenShiftUtils.getInstance().getDeploymentConfigs().stream().filter(dc -> dc.getMetadata().getName().equals("broker-amq")).findFirst()
                .ifPresent(dc -> OpenShiftUtils.getInstance().deleteDeploymentConfig(dc, true));
        OpenShiftUtils.getInstance().getServices().stream().filter(service ->  "syndesis-amq".equals(service.getMetadata().getLabels().get("template"))).findFirst()
                .ifPresent(service -> OpenShiftUtils.getInstance().deleteService(service));
        OpenShiftUtils.getInstance().getImageStreams().stream().filter(is -> "syndesis-amq".equals(is.getMetadata().getLabels().get("template"))).findFirst()
                .ifPresent(is -> OpenShiftUtils.getInstance().deleteImageStream(is));
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
