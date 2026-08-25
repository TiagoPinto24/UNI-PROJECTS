Feature: Creation of a new room

Scenario: An administrator creates a new room
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
When the client gives valid informations for a new room
And calls room/create
Then the client recieves code 201
And a new room is created