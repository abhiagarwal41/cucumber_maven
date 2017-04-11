#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios 
#<> (placeholder)
#""
## (Comments)

#Sample Feature Definition Template

Feature: Test smoke scenario

Scenario: Test login with valid credentials
Given Open firefox and start application
When I enter valid "abhi" and "hello123"
Then user should login successfully

Scenario Outline: Test login with three valid credentials
Given Open firefox and start application
When I enter valid "<username>" and "<password>"
Then user should login successfully

Examples:
|username|password|
|uname1|pass1|
|uname2|pass2|
|uname3|pass3|
