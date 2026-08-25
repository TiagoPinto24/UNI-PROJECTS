Feature: Validate rating score range

  Scenario: client makes call to POST review with valid rating
    Given the review service URL
    And a completed reservation exists
    And the client is associated with the reservation
    When the client calls POST /reviews with:
      | reservationId   | 2 |
      | rating          | 8 |
      | textualDescription | Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home |
    Then the client receives status code 201
    And the review response contains:
      | rating | 8 |

  Scenario: client submits review with minimum valid rating
    Given the review service URL
    And a completed reservation exists
    And the client is associated with the reservation
    When the client calls POST /reviews with:
      | reservationId   | 2 |
      | rating          | 0 |
      | textualDescription | Minimum rating |
    Then the client receives status code 201

  Scenario: client submits review with maximum valid rating
    Given the review service URL
    And a completed reservation exists
    And the client is associated with the reservation
    When the client calls POST /reviews with:
      | reservationId   | 2 |
      | rating          | 10 |
      | textualDescription | Maximum rating |
    Then the client receives status code 201