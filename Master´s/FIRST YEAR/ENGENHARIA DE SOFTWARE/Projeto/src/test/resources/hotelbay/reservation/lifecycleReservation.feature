Feature: Reservation lifecycle states

  Scenario: client makes call to GET reservation lifecycle states
    Given the lifecycle reservation service URL
    When the client calls reservations with statuses
    Then the lifecycle reservation client receives status code 200
    And the response contains:
      | PENDING |
      | CONFIRMED |
      | CANCELED |
      | COMPLETED |
