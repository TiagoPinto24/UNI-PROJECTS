Feature: Failed payment attempt

  Scenario: client makes call to PATCH fail payment for a reservation
    Given the failed payment service URL
    And a failed payment reservation with id 1 exists with status "PENDING"
    And a payment with transaction number 1 exists with status "processing"
    When the client updates payment status to "failed" for payment 1
    Then the failed payment client receives status code 422
    And the failed payment response contains payment status "failed"
    And the reservation status remains "PENDING"