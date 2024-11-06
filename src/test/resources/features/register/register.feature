Feature: Registration

  Background:
    Given I am connected to capacities of the service

    @SmokeTest
  Scenario: Successful registration with all required fields
    When I enter the following correct user data for registration
      | email                    | password                | username       |
      | usuarioprueba1@correo.com | micontraseñaSEGURA!2035 | usuarioprueba1 |
    Then I can see a 200 status code with successful registration message

    @AcceptanceTest
  Scenario: Attempt to register with a duplicate username
    When I enter the following correct user data for registration
      | email                    | password                | username       |
      | usuario2@correo.com      | micontraseñaSEGURA!2035 | usuarioprueba2 |
    And I attempt to register with the same user data again
    Then I see a 409 status code with a message about username already taken


      @AcceptanceTest
    Scenario Outline: Registration fails with an invalid or weak password
      When I enter the following user data for registration
        | email           | password        | username       |
        | usuario3@correo.com | <password>     | usuarioprueba3 |
      Then I see a 400 status code with a message about weak password

      Examples:
        | password           |
        |                    |
        | 12345              |
        | password123        |
        | CONTRASEÑA_SEGURA  |
        | contraseña_segura! |

    @AcceptanceTest
    Scenario: Attempt to register with a duplicate email
      When I enter the following correct user data for registration
        | email                    | password                | username       |
        | usuario1@correo.com      | micontraseñaSEGURA!2035 | usuarioprueba1 |
      And I attempt to register with the same email again
      Then I see a 409 status code with a message about email already taken
