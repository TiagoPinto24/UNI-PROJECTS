Feature: Store payment history

  Scenario: client makes call to GET payment history by reservation
    Given the payment service URL
    And a reservation with past payments exists
    When the client calls payments with 1
    Then the client receives status code 200
    And the response contains a list of payments