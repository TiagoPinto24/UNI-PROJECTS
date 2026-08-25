Feature: Verify the review service

  Scenario: client makes call to POST create review associated with hotel and guest
    Given the review service URL
    And a completed reservation exists for association
    And the client is associated with the reservation
    And the hotel is associated with the reservation
    When the associate client calls POST /reviews with:
      | reservationId      | 2 |
      | hotel              | Evora Hotel |
      | nameGuest          | Jorge |
      | textualDescription | Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home |
      | rating             | 8 |
    Then the associate client receives status code 201
    And the associate response contains reservationId 2
    And the associate response contains a hotel "Evora Hotel"
    And the associate response contains guest called "Jorge"
    And the associate response contains review with description "Had a wonderful stay! The location was perfect for sightseeing, and the staff made us feel at home"
    And the associate response contains rating 8