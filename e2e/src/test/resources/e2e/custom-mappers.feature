Feature: Custom mappers

  Scenario: Use a static custom mapper
    Given custom values
      | code | codes    |
      | ABC  | XY, ZZ   |
    Then the mapped value is
      """
      [CustomValues[code=Code[value=ABC], codes=[Code[value=XY], Code[value=ZZ]], reference=null]]
      """

  Scenario: Use a custom mapper called on the step definition instance
    Given the reference prefix is "invoice"
    And custom values
      | reference |
      | R1        |
    Then the mapped value is
      """
      [CustomValues[code=null, codes=null, reference=Reference[prefix=invoice, value=R1]]]
      """
