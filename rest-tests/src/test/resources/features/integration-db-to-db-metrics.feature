@integrations-db-to-db-metrics
Feature: Test functionality of monitoring metrics system, part 1.

  Background: Clean application state
    Given clean application state
    And remove all records from DB

  @integrations-db-db-metrics
  Scenario: DB to DB integration with basic metrics monitoring
    Then inserts into "contact" table
      | Josef2 | Stieranka | Istrochem | db |

    When create start DB periodic sql invocation action step with query "SELECT * FROM CONTACT" and period "5000" ms
    And create mapper step using template: "db-db-monitoring"
    And create finish DB invoke sql action step with query "INSERT INTO TODO (task, completed) VALUES (:#task, 0)"

    Then create integration with name: "DB to DB metrics rest test"
    Then wait for integration with name: "DB to DB metrics rest test" to become active

    Then validate that number of all todos with task "Josef2" is "1", period in ms: "5000"

#    monitoring metrics part:
    Then validate that number of all messages through integration "DB to DB metrics rest test" is greater than "3", period in ms: "10000"