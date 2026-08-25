Feature: Modify an existing reservation

  Scenario: client makes call to PUT modify an existing reservation
    Given the modify reservation service URL
    And an existing modify reservation with id 1
    And the client wants to change the dates to "2026-07-06" and "2026-07-13"
    When the client calls PUT /reservations/1 with:
      | checkIn  | 2026-07-06 |
      | checkOut | 2026-07-13 |
    Then the modify reservation client receives status code 200
    And the reservation is updated with the new dates