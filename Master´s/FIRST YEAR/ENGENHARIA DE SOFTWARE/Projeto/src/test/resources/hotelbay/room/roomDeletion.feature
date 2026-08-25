Feature: Removal of a room from the system

Scenario: An administrator deletes a room from the system
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
And a room with the number 121 belongs to Grand Hotel
When the client calls room/delete
And the client recieves code 200
And the room is deleted from the system