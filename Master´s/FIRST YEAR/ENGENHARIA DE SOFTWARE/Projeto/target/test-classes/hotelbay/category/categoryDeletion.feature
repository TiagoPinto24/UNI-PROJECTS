Feature: Deletion of a category

Scenario: Admin deletes a category
Given the service URL
And the current client has a valid administrator account
And a category with the name Suíte belongs to the existing admin
When the client calls category/delete
Then the client recieves code 200
And the category is deleted from the system