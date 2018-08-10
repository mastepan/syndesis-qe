# @sustainer: mastepan@redhat.com

@servicenow
Feature: Servicenow Connector

  Background: Clean application state
    Given clean application state
    Given log into the Syndesis
#    Given clean ServiceNow tableName table
#    Given insert data to ServiceNow tableName table


    Given created connections
      | ServiceNow | Servicenow | Servicenow | Desc |

    And navigate to the "Home" page


#
#  1. Check that slack message exists
#
  @servicenow-retrieve-data
  Scenario: Retrieve and write data

    And navigate to the "Home" page
    #move the following data to credentials.json
    When create new API connector
      | source   | file          | swagger/connectors/todo.swagger.yaml |
      | security | authType      | HTTP Basic Authentication            |
      | details  | connectorName | todo                                 |
      | details  | routeHost     | todo                                 |
      | details  | baseUrl       | /api                                 |

    Then creates connections without validation
      | todo | todo | Todo connection |  |


    And navigate to the "Home" page

    # create integration
    And click on the "Create Integration" button to create a new integration.

    When select the "Servicenow" connection
    And select "Retrieve Record" integration action
    And fill in values
    #create table in SN
      | The table name | Syndesis Test |

    And click on the "Done" button

    Then select the "Todo connection" connection
    And select "Create new task" integration action

    Then click on the "Add a Step" button
    When select "Data Mapper" integration step

    Then create data mapper mappings
      | u_name | body.task |

    And click on the "Done" button

    Then click on the "Publish" button
    And set integration name "Todo integration"
    Then click on the "Publish" button

    When navigate to the "Integrations" page
    Then wait until integration "Todo integration" gets into "Running" state

    When she goes to Todo app

# check data loaded from servicenow to TODO app