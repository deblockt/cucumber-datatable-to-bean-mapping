Feature: Mapping


  Scenario: Map bean with primitive
    Given a step with only one object
      | string | integer | integers      |
      | s      | 10      | 1, 3, 4, 5, 6 |
    And a step with a list
      | string | integer | enumValue |
      | s      | 10      |           |
      | a      | 11      | VALUE1    |
    And a step with a list of record
      | string | integer | integers | customDTO | aMap       |
      | s      | 10      |          |           |            |
      | a      | 11      | 3, 1039  | test      | {"a": "b"} |


  Scenario: Failed due to typo on column
    Given a step with a list
      | string | integr |
      | s      | 10     |

  Scenario: Failed due to missing column
    Given a step with a list
      | string |
      | s      |