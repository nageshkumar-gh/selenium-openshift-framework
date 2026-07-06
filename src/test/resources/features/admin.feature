@admin
Feature: OrangeHRM Admin Module
  As an HR administrator
  I want to access the Admin module

  Background:
    Given the user has logged in successfully navigating to the "Dashboard"
    When the user navigates to the "Admin" module
    Then the top bar should show the "Admin" page title

  @regression
  Scenario: Search for an existing system user by username
    When the user searches for the system user with username "Admin"
    Then at least one user record should be displayed in the results

