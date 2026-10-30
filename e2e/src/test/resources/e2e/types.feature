Feature: Simple Java types

  Scenario: Map primitive types
    Given primitive values
      | int | long       | short | float | double | boolean |
      | 12  | 9000000000 | 7     | 1.5   | 10.25  | true    |
    Then the mapped value is
      """
      [PrimitiveValues[anInt=12, aLong=9000000000, aShort=7, aFloat=1.5, aDouble=10.25, aBoolean=true]]
      """

  Scenario: Map wrapper types
    Given wrapper values
      | integer | long       | short | float | double | boolean | big decimal | big integer                    |
      | 12      | 9000000000 | 7     | 1.5   | 10.25  | false   | 10.05       | 123456789012345678901234567890 |
    Then the mapped value is
      """
      [WrapperValues[anInteger=12, aLong=9000000000, aShort=7, aFloat=1.5, aDouble=10.25, aBoolean=false, aBigDecimal=10.05, aBigInteger=123456789012345678901234567890, aString=null, anUuid=null, level=null]]
      """

  Scenario: Use a comma as decimal separator
    Given wrapper values
      | float | double | big decimal |
      | 1,5   | 10,25  | 10,05       |
    Then the mapped value is
      """
      [WrapperValues[anInteger=null, aLong=null, aShort=null, aFloat=1.5, aDouble=10.25, aBoolean=null, aBigDecimal=10.05, aBigInteger=null, aString=null, anUuid=null, level=null]]
      """

  Scenario: Map an empty cell to null
    Given wrapper values
      | integer | boolean | string |
      |         |         |        |
    Then the mapped value is
      """
      [WrapperValues[anInteger=null, aLong=null, aShort=null, aFloat=null, aDouble=null, aBoolean=null, aBigDecimal=null, aBigInteger=null, aString=null, anUuid=null, level=null]]
      """

  Scenario: Map a string, an UUID and an enum
    Given wrapper values
      | string      | uuid                                 | level |
      | hello world | 123e4567-e89b-12d3-a456-426614174000 | GOLD  |
    Then the mapped value is
      """
      [WrapperValues[anInteger=null, aLong=null, aShort=null, aFloat=null, aDouble=null, aBoolean=null, aBigDecimal=null, aBigInteger=null, aString=hello world, anUuid=123e4567-e89b-12d3-a456-426614174000, level=GOLD]]
      """
