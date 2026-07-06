@navigation @sanity
Feature: OrangeHRM Navigation
  In order to verify the main sidebar modules
  As an authenticated user
  I want to navigate to each module from one place

  Background:
    Given the user has logged in successfully navigating to the "Dashboard"

  Scenario Outline: Navigate to the <module> module
    When the user navigates to the "<module>" module
    Then the top bar should show the "<module>" page title

    Examples:
      | module      |
      | PIM         |
      | Admin       |
      | Leave       |
      | Time        |
      | Recruitment |
      | Performance |
      | Directory   |
      | Claim       |
      | Buzz        |

