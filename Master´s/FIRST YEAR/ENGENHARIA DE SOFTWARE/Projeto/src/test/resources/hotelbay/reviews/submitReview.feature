Feature: Guest submits a review after completed stay

  Scenario: client makes call to POST submit a review after stay
    Given the review service URL
    And an existing completed reservation with no review
    And the client is associated with the reservation
    When the submit client calls POST /reviews with:
      | reservationId      | 2 |
      | textualDescription | Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home |
      | rating             | 8 |
    Then the submit client receives status code 201
    And the submit response contains review with description "Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home"
    And the submit response contains rating 8