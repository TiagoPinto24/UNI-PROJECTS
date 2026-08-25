Feature: Reservation confirmation after successful payment

  Scenario: client updates payment status to successful
    Given the payment service URL
    And a payment exists for a reservation with id 1
    When the client calls PATCH /payments with id 1 and status "successful"
    Then the confirm reservation client receives status code 200
    And the response contains payment status "successful"
    And the reservation status becomes "CONFIRMED"