Feature: Prevent reservation for an already booked room

  Scenario: client makes call to POST create reservation for an already booked room
    Given the prevent reservation service URL
    And a room is already reserved between "2026-07-01" and "2026-07-05"
    When the client calls /reservations with:
      | checkIn      | 2026-07-03 |
      | checkOut     | 2026-07-06 |
      | numberGuests | 2          |
    Then the prevent reservation client receives status code 409
    And the prevent reservation response contains message "Room is not available"
    