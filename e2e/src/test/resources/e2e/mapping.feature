Feature: Mapping datatables to beans

  Scenario: Map a single record
    Given a customer
      | first name | last name | vip  |
      | John       | Doe       | true |
    Then the mapped value is
      """
      Customer[firstName=John, lastName=Doe, vip=true]
      """

  Scenario: Use the default value of an optional column
    Given customers
      | first name | last name |
      | John       | Doe       |
      | Jane       | Smith     |
    Then the mapped value is
      """
      [Customer[firstName=John, lastName=Doe, vip=false], Customer[firstName=Jane, lastName=Smith, vip=false]]
      """

  Scenario: Map a class with public fields
    Given products
      | name  | stock |
      | apple | 3     |
      | pear  |       |
    Then the mapped value is
      """
      [Product[name=apple, stock=3], Product[name=pear, stock=null]]
      """
