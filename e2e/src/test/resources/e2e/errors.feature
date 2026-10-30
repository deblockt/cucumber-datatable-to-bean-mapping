Feature: Mapping errors

  Scenario: Unknown column
    Given customers
      | first name | last nam |
      | John       | Doe      |

  Scenario: Missing mandatory column
    Given customers
      | first name |
      | John       |

  Scenario: Invalid number
    Given wrapper values
      | integer |
      | twelve  |

  Scenario: Unknown enum value
    Given wrapper values
      | level  |
      | BRONZE |

  Scenario: Invalid UUID
    Given wrapper values
      | uuid       |
      | not-a-uuid |

  Scenario: Invalid date
    Given date values
      | local date |
      | tomorrow   |

  Scenario: Invalid JSON for a map column
    Given collection values
      | scores    |
      | {"math": |

  Scenario: Custom mapper throws an exception
    Given custom values
      | code |
      | abc  |
