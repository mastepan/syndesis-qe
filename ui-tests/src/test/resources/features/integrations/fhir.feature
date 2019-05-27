# @sustainer: mastepan@redhat.com

@ui
@fhir
Feature: Integration - FHIR

  Background: Clean application state
    Given clean application state
    Given log into the Syndesis
    And created connections
      | FHIR | FHIR | | Description |

    # scenario done
  @fhir-create
  Scenario: Creata and Read operations

    When navigate to the "Home" page
    And click on the "Create Integration" link to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

#start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

#finish connection
    When select the "Log" connection
    And fill in values by element data-testid
      | contextloggingenabled | true |
      | bodyloggingenabled | true |
    Then click on the "Done" button

#FHIR create
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Create" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

#datamapper before FHIR create
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Patient.name.given.value |
      | last_name | Patient.name.family.value |
    And click on the "Done" button

#FHIR read
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "Read" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

#datamapper before FHIR read
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button
#run the integration
    When click on the "Save" link
    And set integration name "FHIR_read"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_read" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_read" contains string "<name><family value="Jackson"></family><given value="Joe">"


#scenario done
  @wip
  @fhir-delete
  Scenario: Create delete and read operations

    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

  #start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

  #finish connection
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

  #FHIR create
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Create" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR create
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Patient.name.given.value |
      | last_name | Patient.name.family.value |
    And click on the "Done" button

  #FHIR delete
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR delete
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #FHIR read
    When add integration step on position "3"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR read
    When add integration step on position "3"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #run the integration
    When click on the "Save" button
    And set integration name "FHIR_delete"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_delete" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_delete" contains string "<TODO: COMPLETE THIS CHECK>"


    #scenario done
    #can't be mapped by datamapper, missing resource id field
  @wip
  @fhir-update
  Scenario: Create update and read operations

    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

  #start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

  #finish connection
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

  #FHIR create
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Create" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR create
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Patient.name.given.value |
      | last_name | Patient.name.family.value |
    And click on the "Done" button

  #FHIR update
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "Update" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR update
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #FHIR read
    When add integration step on position "3"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR read
    When add integration step on position "3"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #run the integration
    When click on the "Save" button
    And set integration name "FHIR_update"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_delete" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_update" contains string "<TODO: COMPLETE THIS CHECK>"

#scenario done
  @WIP
  @fhir-patch
  Scenario: Create patch and read operations

    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

  #start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

  #finish connection
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

  #FHIR create
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Create" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR create
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Patient.name.given.value |
      | last_name | Patient.name.family.value |
    And click on the "Done" button

  #FHIR patch
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "Patch" integration action
    And fill in resource type with "Patient" value
    And fill in JSON patch
    | TODO [{"op":"replace", "path":""}] |
    And click on the "Next" button

  #datamapper before FHIR patch
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #FHIR read
    When add integration step on position "3"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR read
    When add integration step on position "3"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #run the integration
    When click on the "Save" button
    And set integration name "FHIR_delete"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_delete" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_delete" contains string "<TODO: COMPLETE THIS CHECK>"

    #scenario done
  @WIP
  @fhir-transaction
  Scenario: Transaction and read operations

    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

#start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

#finish Log connection
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

#FHIR transaction
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Transaction" integration action
    And select resource types
      | Basic   |
      | Patient |
    And click on the "Next" button

#datamapper before FHIR transaction
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Transaction.Patient.name.given.value  |
      | last_name  | Transaction.Patient.name.family.value |
      | company    | Transaction.Basic.text.status.value   |
    And click on the "Done" button

#FHIR read
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "read" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

#datamapper before FHIR transaction
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - io.syndesis:fhir-transaction-connector.Transaction.Patient.id.value | id |
    And click on the "Done" button

#Log
    When add integration step on position "4"
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

#FHIR read
    When add integration step on position "5"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Basic" value
    And click on the "Next" button

#datamapper before FHIR read
    When add integration step on position "5"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - io.syndesis:fhir-transaction-connector.Transaction.Basic.id.value | id |
    And click on the "Done" button

#run the integration
    When click on the "Save" button
    And set integration name "FHIR_delete"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_delete" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_delete" contains string "<TODO: COMPLETE THIS CHECK - Basic>"
    Then validate that logs of integration "FHIR_delete" contains string "<TODO: COMPLETE THIS CHECK - Patient>"


  @WIP
  @fhir-search
  Scenario: Create search and read operations

    When navigate to the "Home" page
    And click on the "Create Integration" button to create a new integration.
    Then check visibility of visual integration editor
    And check that position of connection to fill is "Start"

  #start connection
    When select the "PostgresDB" connection
    And select "Periodic SQL invocation" integration action
    Then check "Next" button is "Disabled"
    Then fill in periodic query input with "SELECT * FROM CONTACT" value
    Then fill in period input with "10" value
    Then select "Minutes" from sql dropdown
    And click on the "Next" button

  #finish connection
    When select the "Log" connection
    And fill in values
      | Message Context | true |
      | Message Body    | true |
    Then click on the "Done" button

  #FHIR create
    When add integration step on position "0"
    When select the "FHIR" connection
    And select "Create" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR create
    When add integration step on position "0"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | first_name | Patient.name.given.value |
      | last_name | Patient.name.family.value |
    And click on the "Done" button

  #FHIR patch
    When add integration step on position "2"
    When select the "FHIR" connection
    And select "Patch" integration action
    And fill in resource type with "Patient" value
    And fill in JSON patch
      | TODO [{"op":"replace", "path":""}] |
    And click on the "Next" button

  #datamapper before FHIR patch
    When add integration step on position "2"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #FHIR read
    When add integration step on position "3"
    When select the "FHIR" connection
    And select "Delete" integration action
    And fill in resource type with "Patient" value
    And click on the "Next" button

  #datamapper before FHIR read
    When add integration step on position "3"
    And select "Data Mapper" integration step
    Then check visibility of data mapper ui
    And create data mapper mappings
      | 3 - Outcome.id.value | id |
    And click on the "Done" button

  #run the integration
    When click on the "Save" button
    And set integration name "FHIR_delete"
    And publish integration
    Then Integration "FHIR_read" is present in integrations list
    And wait until integration "FHIR_delete" gets into "Running" state

    When sleep for "10000" ms
    Then validate that logs of integration "FHIR_delete" contains string "<TODO: COMPLETE THIS CHECK>"
