Feature: Altering a hotel specifications

Scenario: Admin alter a hotel specifications
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
When the information given is valid to alter the hotel
And the client calls /hotel/changeInfo
Then the client recieves code 200
And the hotel specifications are changed