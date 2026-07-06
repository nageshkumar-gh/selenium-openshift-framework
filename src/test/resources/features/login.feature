@auth @sanity
Feature: OrangeHRM Login
  In order to access OrangeHRM features securely
  As a valid or invalid user
  I want the system to allow or deny login appropriately

  Background:
    Given the user is on the OrangeHRM login page

  @regression
  Scenario: Valid login with correct credentials
    #When the user logs in with username "Admin" and password "admin123"
    When the user logs in with correct credentials
    Then the user should be logged in successfully navigating to the "Dashboard"

  @regression
  Scenario: Invalid login with wrong password
    When the user logs in with wrong credentials
    Then an authentication error "Invalid credentials" message should be displayed

  @regression
  Scenario: Login with empty fields
    When the user logs in with username "" and password ""
    Then a "Required" field validation message should be displayed
