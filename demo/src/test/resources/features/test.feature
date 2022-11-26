Feature: Mapping


  Scenario: Map bean with primitive
    Given a step with only one object
      | string | integer |
      | s      | 10      |
    And a step with a list
      | string | integer |
      | s      | 10      |
      | a      | 11      |


  Scenario: Failed due to typo on column
    Given a step with a list
      | string | integr |
      | s      | 10     |

  Scenario: Failed due to missing column
    Given a step with a list
      | string |
      | s      |