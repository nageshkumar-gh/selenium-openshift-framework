@buzz
Feature: OrangeHRM Buzz Module
  As an employee
  I want to use the Buzz module

  Background:
    Given the user has logged in successfully navigating to the "Dashboard"
    When the user navigates to the "Buzz" module
    Then the top bar should show the "Buzz" page title

  @regression
  Scenario: Share a post on the Buzz feed
    When the user shares a post with the message "Automation test post - please ignore"
    Then the post "Automation test post - please ignore" should appear at the top of the Buzz feed

