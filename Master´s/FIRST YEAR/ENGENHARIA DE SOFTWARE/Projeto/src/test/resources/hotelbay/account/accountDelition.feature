Feature: Elimination fo account

Scenario: User deletes it's own account
Given the service URL
And the current client has an account
When the client calls /account/delete/me
Then the client recieves code 200
And the account is successfully deleted