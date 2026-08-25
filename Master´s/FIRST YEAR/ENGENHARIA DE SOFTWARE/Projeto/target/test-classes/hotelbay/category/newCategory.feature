Feature: Creating a new category

Scenario: Admin creates a new category
Given the service URL
And the current client has a valid administrator account
When the client gives valid category information
And calls category/create
Then the client recieves code 201
And a new category is created