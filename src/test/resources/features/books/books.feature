
Feature: Books
  # As a user,
  # I want to be able to retrieve a list of books from the system
  # so that I can browse and explore available books.

  Background:
    Given I am connected to the service

  @SmokeTest
  Scenario: Retrieve list of books
    When I request the list of books from the "/books/v1" endpoint
    Then I can see a 200 status code with a list of books