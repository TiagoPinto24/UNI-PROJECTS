Feature: Assigning a room to an existing category

Scenario: An admin assigns a room to a category
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
And a room with the number 121 belongs to Grand Hotel
And a category with the name Suíte belonging to the existing admin
When the client gives the category in the body of the request
And the client calls room/changeInfo
Then the client recieves code 200
And the room 121 should have the category named Suíte