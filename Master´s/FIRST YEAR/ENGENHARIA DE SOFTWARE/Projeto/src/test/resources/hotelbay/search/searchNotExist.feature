Feature: Searching for a room and not geting a result

Scenario: A user searches for a room with parameters that don't match any existing room
Given the service URL
And the current client has an account
And no room is available between 1/1/2027 and 8/1/2027 in the location Lisbon
When the client calls search rooms with location Lisbon and checkin 1/1/2027 and checkout 8/1/2027
Then the client recieves code 200
And the response should not contain any rooms