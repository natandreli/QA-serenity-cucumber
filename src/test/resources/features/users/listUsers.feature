Feature: List users

  Background:
    Given I'm connected to the service

  Scenario: Successfully retrieve the list of users
    When I request the list of users
    Then I should receive a 200 status code with the list of users
    And the list should contain the following users:
      | email                     | username       |
      | mail1@mail.com            | name1          |
      | mail2@mail.com            | name2          |
      | admin@mail.com            | admin          |
      | usuarioprueba1@correo.com | usuarioprueba1 |
      | usuario3@correo.com       | usuarioprueba3 |
      | usuario2@correo.com       | usuarioprueba2 |