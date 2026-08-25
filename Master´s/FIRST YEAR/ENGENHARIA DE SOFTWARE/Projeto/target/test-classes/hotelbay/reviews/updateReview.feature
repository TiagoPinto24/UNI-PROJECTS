Feature: Update hotel rating after new review

  Scenario: client makes call to update hotel rating
    Given the review reservation service URL
    And an existing review reservation with id 1
    And the client wants to change the rate to "6"
    When the client calls PUT /reviews/1 with:
      | rating | 6 |
    Then the update client receives status code 200
    And the rating is updated with the score