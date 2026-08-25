Feature: Preserve reservation history

  Scenario: client makes call to GET reservation history
    Given the preserve reservation service URL
    And a guest with past reservations exists
    When the client calls GET /guests/1/reservations
    Then the preserve reservation client receives status code 200
    And the response contains a list of past reservations