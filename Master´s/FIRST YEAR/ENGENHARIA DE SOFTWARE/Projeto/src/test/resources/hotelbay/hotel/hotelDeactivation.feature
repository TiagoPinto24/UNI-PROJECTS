Feature: An hotel can be deactivated by the respective admin

Scenario: An hotel is deactivated
Given the service URL
And the current client has a valid administrator account
And a hotel with the name Grand Hotel wich is managed by the current client
When the information given is valid to alter the hotel activation state
And client calls /hotel/changeInfo
Then the client recieves code 200
And the hotel should be deactivated