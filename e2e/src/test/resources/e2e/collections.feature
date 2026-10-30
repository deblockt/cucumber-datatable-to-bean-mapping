Feature: Collections and maps

  Scenario: Map lists
    Given collection values
      | integer list | double list | level list   |
      | 1, 3, 5      | 1.5, 2.5    | SILVER, GOLD |
    Then the mapped value is
      """
      [CollectionValues[integerList=[1, 3, 5], doubleList=[1.5, 2.5], levelList=[SILVER, GOLD], integerSet=null, stringCollection=null, scores=null, products=null]]
      """

  Scenario: Map a set and a collection
    Given collection values
      | integer set | string collection |
      | 3, 1, 3, 2  | apple, pear       |
    Then the mapped value is
      """
      [CollectionValues[integerList=null, doubleList=null, levelList=null, integerSet=[1, 2, 3], stringCollection=[apple, pear], scores=null, products=null]]
      """

  Scenario: Map a JSON object to a map
    Given collection values
      | scores                          | products                                  |
      | {"math": [12, 15], "music": []} | {"apple": {"name": "apple", "stock": 3}} |
    Then the mapped value is
      """
      [CollectionValues[integerList=null, doubleList=null, levelList=null, integerSet=null, stringCollection=null, scores={math=[12, 15], music=[]}, products={apple=ProductStock[name=apple, stock=3]}]]
      """
