Feature: Creation of a new account

Scenario: User creates a new account
Given the service URL
And the current client does not have an account
When the client gives valid information to create a new account
And calls /account/register/user
Then the client recieves code 201
And a new account is created