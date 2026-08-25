Feature: Alteration of user information

Scenario: User alters it's own email
Given the service URL
And the current client has an account 
When the client gives valid information to alter the account
And calls /account/changeInfo/me
Then the client recieves code 200 
And the informtaion is successfully changed