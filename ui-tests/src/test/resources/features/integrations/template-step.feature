# @sustainer: mastepan@redhat.com

@wip
@template-integration-step
Feature: Template integration step

  Background: Clean application state
    Given clean application state
    Given clean SF contacts related to TW account: "twitter_talky"
    Given log into the Syndesis
    #?? Timer to DB
#    Given created connections
#      | Twitter    | Twitter Listener | Twitter Listener | SyndesisQE Twitter listener account |
#      | Salesforce | QE Salesforce    | QE Salesforce    | SyndesisQE salesforce test          |

  @cancel
  @from-file
  @from-URL
  @create
  Scenario: Create and use
    # create integration
    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    # select start connection
    When select the "Timer" connection
    And select "Simple Timer" integration action
    #...

    # select finish connection

    # add Template step
    When click on the "Add a Step" button
    And select "Template" integration step
    And upload template
    sourceType | source

    And click on the "Save" button

    # publish integration
    When click on the "Publish" button
    And set integration name "Template"
    And click on the "Publish" button
    # assert integration is present in list
    Then check visibility of "Template" integration details
    And navigate to the "Integrations" page
    And Integration "Twitter to Salesforce E2E" is present in integrations list
    # wait for integration to get in active state
    Then wait until integration "Twitter to Salesforce E2E" gets into "Running" state
    #check the integration


