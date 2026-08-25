Feature: Verify the reservation information

  Scenario: client makes call to GET reservation information
    Given the info reservation service URL
    And an info reservation exists
    When the info reservation client calls GET reservation details
    Then the info reservation client receives status code 200
    And the info reservation response contains fields:
    | room |
    | guest |
    | checkInDate |
    | checkOutDate |
    | numberGuests |