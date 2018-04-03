@api-connector-integration
Feature: API connector test

  Background:
    Given "Camilla" logs into the Syndesis
    Given clean application state

  @DB-to-TODO-custom-api-connector-integration
  Scenario: CREATE custom API client connector from swagger downloaded from url
    When Camilla creates new API connector
      | source    | file            | swagger/connectors/todo.swagger.yaml  |
      | security  | authType        | HTTP Basic Authentication             |
      | details   | connectorName   | Todo connector                        |
      | details   | routeHost       | todo                                  |
      | details   | baseUrl         |                                       |

    And she opens the API connector "Todo connector" detail

    Then she finds blank blabla at blabla

