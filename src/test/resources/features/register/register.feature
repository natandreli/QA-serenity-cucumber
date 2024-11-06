Feature: Registration

  Background:
    Given I am connected to capacities of the service

    @SmokeTest
  Scenario: Successful registration with all required fields
    When I enter the correct user data:
      | email                    | password                | username       |
      | usuarioprueba1@correo.com | micontraseñaSEGURA!2035 | usuarioprueba1 |
    Then I can see a 200 status code with successful registration message

  @Regression
  Scenario Outline: Validate registration with invalid data
    When I enter the following user data:
      | email   | password   | username   |
      | <email> | <password> | <username> |
    Then I should see a <statusCode> status code
    And I should see the error message "<errorMessage>"

    Examples:
      | email                    | password             | username      | statusCode
      | invalid.email           | ValidPass123!        | validuser     | 400
      | test@email.com          | short                | validuser     | 400
      | test@email.com          | ValidPass123!        |               | 400
      | existing@email.com      | ValidPass123!        | existinguser  | 409
      |                         | ValidPass123!        | validuser     | 400
      | test@email.com          |                      | validuser     | 400

  @Regression
  Scenario: Registration with password requirements
    When I enter user data with password requirements:
      | email           | password      | username    | shouldPass |
      | test1@mail.com  | NoNumbers     | testuser1   | false      |
      | test2@mail.com  | nouppercas3!  | testuser2   | false      |
      | test3@mail.com  | NoSpecial3    | testuser3   | false      |
      | test4@mail.com  | Valid1Pass!   | testuser4   | true       |
    Then the registration should be validated according to password requirements

  @Regression
  Scenario: Registration with username restrictions
    When I enter a username with special characters "test@user!"
    Then I should see a 400 status code
    And I should see the error message "Username can only contain letters, numbers and underscores"