package io.syndesis.qe.rest.tests.integrations;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Given;
import io.syndesis.common.model.filter.FilterPredicate;
import io.syndesis.common.model.integration.Step;
import io.syndesis.common.model.integration.StepKind;
import io.syndesis.qe.rest.tests.entities.StepDefinition;
import io.syndesis.qe.rest.tests.storage.StepsStorage;
import io.syndesis.qe.utils.FilterRulesBuilder;
import io.syndesis.qe.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * This class contains intermediate step definition methods. It can for instance create a mapping or filter steps.
 *
 * Jan 15, 2018 Red Hat
 *
 * @author tplevko@redhat.com
 */
@Slf4j
public class IntermediateSteps {

    @Autowired
    private StepsStorage steps;

    public IntermediateSteps() {
    }

    @Given("^create basic TW to SF filter step")
    public void createBasicFilterStep() {
        final Step basicFilter = new Step.Builder()
                .stepKind(StepKind.ruleFilter)
                .configuredProperties(TestUtils.map(
                        "type", "rule",
                        "predicate", FilterPredicate.AND.toString(),
                        "rules", new FilterRulesBuilder().addPath("text").addValue("#backendTest").addOps("contains").build()
                ))
                .build();
        steps.getStepDefinitions().add(new StepDefinition(basicFilter));
    }
}
