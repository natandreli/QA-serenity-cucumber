Feature: Login

  Background:
    Given I am connected to the service capabilities

  @SmokeTest
  Scenario: Successful login with valid credentials
    When I submit the login data:
      | username       | password           |
      | name1          | pass1              |
    Then I should receive a 200 status code with successful login message

  @RegressionTest
  Scenario: Login with incorrect password
    When I submit the login data:
      | username       | password           |
      | name1          | wrongpassword123   |
    Then I should receive a 401 status code with invalid credentials message

  @RegressionTest
  Scenario: Login with non-existent username
    When I submit the login data:
      | username       | password           |
      | nonExistent    | pass1              |
    Then I should receive a 404 status code with user not found message

  @NegativeTest
  Scenario: Login with missing password
    When I submit the login data:
      | username       | password           |
      | name1          |                    |
    Then I should receive a 400 status code with missing password message

  @NegativeTest
  Scenario: Login with missing username
    When I submit the login data:
      | username       | password           |
      |                | pass1              |
    Then I should receive a 400 status code with missing username message

  @NegativeTest
  Scenario: Login with empty username and valid password
    When I submit the login data:
      | username | password |
      | ""       | pass1    |
    Then I should receive a 400 status code with empty username message

  @NegativeTest
  Scenario: Login with valid username and empty password
    When I submit the login data:
      | username | password |
      | name1    | ""       |
    Then I should receive a 400 status code with empty password message

  @NegativeTest
  Scenario: Login with empty username and empty password
    When I submit the login data:
      | username | password |
      | ""       | ""       |
    Then I should receive a 400 status code with empty credentials message