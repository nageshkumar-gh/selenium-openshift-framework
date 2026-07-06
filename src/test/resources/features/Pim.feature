@pim
Feature: OrangeHRM PIM Module
  In order to manage employee information
  As an HR administrator
  I want to access the PIM module and add new employees

  Background:
    Given the user has logged in successfully navigating to the "Dashboard"

  @regression
  Scenario: Add a new employee with valid details
    When the user clicks on Add Employee button
    And the user enters first name "John"
    And the user enters middle name "Patrick"
    And the user enters last name "Doe"
    And the user saves the employee
    Then the employee should be added successfully
    And the employee ID should be generated

  @regression
  Scenario: Add employee with only required fields
    When the user clicks on Add Employee button
    And the user enters first name "Jane"
    And the user enters last name "Smith"
    And the user saves the employee
    Then the employee should be added successfully

  @regression
  Scenario: Add employee with empty required fields
    When the user clicks on Add Employee button
    And the user saves the employee without entering name
    Then a validation error message should be displayed for required fields

  @regression
  Scenario: View employee list in PIM
    When the user should see the employee list
    Then the employee list should contain at least one employee

  @regression
  Scenario: Search for an employee in PIM
    When the user enters employee name "Admin" in search field
    And the user clicks search button
    Then the search results should display matching employees

  @regression
  Scenario: Add employee with full details including contact information
    When the user clicks on Add Employee button
    And the user enters first name "Michael"
    And the user enters last name "Johnson"
    And the user enters employee ID "EMP001"
    And the user saves the employee
    Then the employee should be added successfully
    And the employee record should be accessible for editing

