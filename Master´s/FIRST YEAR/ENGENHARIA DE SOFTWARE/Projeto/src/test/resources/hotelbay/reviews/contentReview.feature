Feature: Review must contain text and rating

  Scenario: client makes call to POST create review with text and rating
    Given the review service URL
    And a completed reservation exists
    And the client is associated with the reservation
    When the client calls POST /reviews with:
      | reservationId      | 1 |
      | textualDescription | Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home |
      | rating             | 8 |
    Then the client receives status code 201
    And the content review is created successfully