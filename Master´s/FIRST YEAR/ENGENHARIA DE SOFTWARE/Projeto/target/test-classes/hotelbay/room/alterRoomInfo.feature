Feature: Altering a room's information

Scenario: An administrator Alters a room's informations
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
And a room with the number 121 belongs to Grand Hotel
When the client gives valid information to alter the room
And calls room/changeInfo
Then the client recieves code 200
And the information related to the room 121 is changed