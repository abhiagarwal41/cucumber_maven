

Feature: Test smoke scenario

@web
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
