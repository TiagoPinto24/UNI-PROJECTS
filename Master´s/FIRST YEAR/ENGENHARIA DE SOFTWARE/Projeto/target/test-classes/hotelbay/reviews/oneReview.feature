Feature: Only one review per reservation

  Scenario: client makes call to POST submit review for an already reviewed reservation
    Given the review service URL
    And a completed reservation exists
    And the client is associated with the reservation
    And the reservation already has a review
    When the client calls POST /reviews with:
      | reservationId | 1 |
      | textualDescription | Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home |
      | rating        | 8 |
    Then the client receives status code 409
    And the response contains message "Reservation already reviewed"