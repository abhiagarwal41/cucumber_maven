Feature: Test facebook invalid login

 # Scenario: Test login with invalid credentials
  #  Given Open Chrome and Start Application
   # When I enter invalid usename and password
    #  | Fields   | Values   |
     # | username | abhishek |
     # | password | hello123 |
   # And Click on LogIn button
   # Then User should not be able to log in

  Scenario Outline: Test different facebook links
    Given Open Chrome and Start Application
    When I click on <Link>
    Then I check I am on <Title>
    And I close the browser

    Examples: 
      | Link    | Title                 |
      | Places  | Discover great places |
      | Games   | Games                 |
      | Careers | Facebook Careers      |
