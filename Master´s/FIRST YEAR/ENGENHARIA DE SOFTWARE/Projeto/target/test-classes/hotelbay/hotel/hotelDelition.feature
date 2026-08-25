Feature: Delition of a hotel

Scenario: An administrator delete an hotel from the system
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
When the client calls /hotel/delete
Then the client recieves code 200
And the hotel is deleted from the system