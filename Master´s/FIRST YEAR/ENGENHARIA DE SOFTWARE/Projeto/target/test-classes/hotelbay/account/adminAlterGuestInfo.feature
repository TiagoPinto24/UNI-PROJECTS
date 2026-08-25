Feature: Admin can alter guests information

Scenario: Admin alters a guests email
Given the service URL
And the current client has a valid administrator account
And an account with the email user@email.com exists
When the clients gives valid information to alter the user account
And the client calls /account/changeInfo/user
Then the client recieves code 200
And the info of the respective account is changed