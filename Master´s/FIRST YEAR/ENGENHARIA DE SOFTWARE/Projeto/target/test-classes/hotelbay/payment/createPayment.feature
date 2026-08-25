Feature: Create a payment for a reservation

  Scenario: client makes call to POST create payment for a reservation
    Given the create payment service URL
    And a payment reservation exists
    When the create payment client calls POST payments with:
    | reservationId | 1 |
    | amount | 100 |
    | paymentDate | 2026-01-01 |
    Then the create payment client receives status code 200
    And the create payment response contains payment status "PENDING"