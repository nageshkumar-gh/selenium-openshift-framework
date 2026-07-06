@claim
Feature: OrangeHRM Claim Module
  As an employee
  I want to use the Claim module

  Background:
    Given the user has logged in successfully navigating to the "Dashboard"
    When the user navigates to the "Claim" module
    Then the top bar should show the "Claim" page title

  @regression
  Scenario: Submit a new expense claim
    When the user creates a new claim with employee name "Charlotte Smith" event "Travel Allowance", currency "Euro" and remarks "Annual checkup reimbursement"
    Then the claim should be submitted "Successfully"

