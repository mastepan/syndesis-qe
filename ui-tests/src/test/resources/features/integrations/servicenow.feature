# @sustainer: mastepan@redhat.com

@servicenow
Feature: Servicenow Connector

  Background: Clean application state
    Given clean application state
    Given log into the Syndesis
#    Given clean ServiceNow tableName table


    Given created connections
      | ServiceNow | Servicenow | Servicenow | Desc |

    And navigate to the "Home" page


#
#  1. Check that slack message exists
#
  @swrvicenow-retrieve-data
  Scenario: Retrieve and write data

    And navigate to the "Home" page

    # create integration
    And click on the "Create Integration" button to create a new integration.

    When select the "Servicenow" connection
    And select "Retrieve Record" integration action
    And fill in values
      | The table name | u my table |

#  ...map data to TODO
#    And click on the "Next" button
#    And click on the "Done" button
#
#
#    Then check that position of connection to fill is "Finish"
#
#    When select the "QE Slack" connection
#    And select "Channel" integration action
#    And fill in values
#      | Channel | test |
#
#
#    And click on the "Done" button
#
#    When click on the "Add a Step" button
#
#    # add data mapper step
#    And select "Set Body" integration step
#    And fill in values
#      | Body | test message |
#
#
#
#    And scroll "top" "right"
#    And click on the "Done" button
#
#    # finish and save integration
#    When click on the "Save as Draft" button
#    And set integration name "Integration_with_slack"
#    And click on the "Publish" button
#    # assert integration is present in list
#    Then check visibility of "Integration_with_slack" integration details
#    And navigate to the "Integrations" page
#
#    And Integration "Integration_with_slack" is present in integrations list
#    # wait for integration to get in active state
#    Then wait until integration "Integration_with_slack" gets into "Running" state
#
#
#    Then check that last slack message equals "test message" on channel "test"