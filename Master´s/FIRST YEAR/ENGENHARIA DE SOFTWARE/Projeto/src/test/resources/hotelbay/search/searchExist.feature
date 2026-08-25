Feature: Searching for a room and geting a result

Scenario: A user searches for a room with parameters that match an existing room
Given the service URL
And the current client has an account
And a hotel with the name Grand Hotel
And a room with the number 121 available between 1/1/2027 and 8/1/2027 in the location Lisbon belongs to Grand Hotel
When the client calls search rooms with location Lisbon and checkin 1/1/2027 and checkout 8/1/2027
Then the client recieves code 200
And the response should contain at least the room with the number 121
And contain the information that this room is available in this time period