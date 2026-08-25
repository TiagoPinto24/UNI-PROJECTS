Feature: Create reservation for an available room

  Scenario: client makes call to POST create reservation
    Given the create reservation service URL
    And a room is available between "2026-07-01" and "2026-07-05"
    And a registered guest exists
    When the client calls /reservations with valid reservation data
    Then the create reservation client receives status code 201
    And the reservation is created with status "PENDING"