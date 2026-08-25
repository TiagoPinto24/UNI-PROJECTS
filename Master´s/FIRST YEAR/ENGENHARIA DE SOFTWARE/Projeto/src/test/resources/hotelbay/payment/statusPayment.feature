Feature: Payment status affects reservation state

  Scenario: payment status update affects reservation state
    Given the payment status service URL
    And a payment status reservation with id 1 exists with status "PENDING"
    And a payment with id 1 exists for reservation 1 with status "processing"
    When the client updates payment 1 to "successful"
    Then the status payment client receives status code 200
    And the status payment response contains payment status "successful"
    And the reservation with id 1 has status "CONFIRMED"