Feature: Nested objects

  Scenario: Map a nested object
    Given accounts
      | id | first name | last name |
      | 10 | John       | Doe       |
    Then the mapped value is
      """
      [Account[id=10, owner=Owner[firstName=John, lastName=Doe]]]
      """

  Scenario: Prefix the columns of a nested object with the parent name
    Given conversations
      | customer1 id | customer1 first name | customer1 last name | customer2 id | customer2 first name | customer2 last name |
      | 10           | John                 | Doe                 | 11           | Jane                 | Doe                 |
    Then the mapped value is
      """
      [Conversation[customer1=Contact[id=10, personalInformation=PersonalInformation[firstName=John, lastName=Doe]], customer2=Contact[id=11, personalInformation=PersonalInformation[firstName=Jane, lastName=Doe]]]]
      """

  Scenario: An optional nested object without columns is null
    Given conversations
      | customer2 id | customer2 first name | customer2 last name |
      | 11           | Jane                 | Doe                 |
    Then the mapped value is
      """
      [Conversation[customer1=null, customer2=Contact[id=11, personalInformation=PersonalInformation[firstName=Jane, lastName=Doe]]]]
      """
