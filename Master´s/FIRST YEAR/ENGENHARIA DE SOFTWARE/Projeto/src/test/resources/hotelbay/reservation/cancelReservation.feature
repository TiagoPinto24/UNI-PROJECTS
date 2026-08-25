Feature: Cancel a reservation for an available room

  Scenario: client makes call to PATCH cancel a reservation
    Given the cancel reservation service URL
    And an existing reservation with id 1
    When the client calls PATCH /reservations/1 with status "CANCELED"
    Then the cancel reservation client receives status code 200
    And the reservation status is updated to "CANCELED"