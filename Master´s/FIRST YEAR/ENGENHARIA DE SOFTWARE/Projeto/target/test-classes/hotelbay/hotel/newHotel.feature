Feature: Creation of a new hotel

Scenario: Admin creates a new hotel
Given the service URL
And the current client has a valid administrator account
And there is no hotel with the name Grand Hotel
When the client gives valid information for a hotel creation
And calls /hotel/create
Then the client recieves code 201
And a new hotel is created