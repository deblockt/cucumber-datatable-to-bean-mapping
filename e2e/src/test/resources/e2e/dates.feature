# now is fixed to 2024-01-31T10:00:00+02:00 by FixedDateTimeService
Feature: Dates

  Scenario: Map ISO dates
    Given date values
      | local date | local date time     | offset date time          |
      | 2024-05-01 | 2024-05-01T08:30:15 | 2024-05-01T08:30:00+02:00 |
    Then the mapped value is
      """
      [DateValues[localDate=2024-05-01, localDateTime=2024-05-01T08:30:15, offsetDateTime=2024-05-01T08:30+02:00]]
      """

  Scenario: Map now
    Given date values
      | local date | local date time | offset date time |
      | now        | now             | now              |
    Then the mapped value is
      """
      [DateValues[localDate=2024-01-31, localDateTime=2024-01-31T10:00, offsetDateTime=2024-01-31T10:00+02:00]]
      """

  Scenario: Map relative local dates
    Given date values
      | local date      |
      | now + 1 day     |
      | now + 3 days    |
      | now - 2 weeks   |
      | now + 1 month   |
      | now - 1 year    |
    Then the mapped value is
      """
      [DateValues[localDate=2024-02-01, localDateTime=null, offsetDateTime=null], DateValues[localDate=2024-02-03, localDateTime=null, offsetDateTime=null], DateValues[localDate=2024-01-17, localDateTime=null, offsetDateTime=null], DateValues[localDate=2024-02-29, localDateTime=null, offsetDateTime=null], DateValues[localDate=2023-01-31, localDateTime=null, offsetDateTime=null]]
      """

  Scenario: Map relative date times
    Given date values
      | local date time    | offset date time   |
      | now + 2 hours      | now - 30 minutes   |
      | now + 15 seconds   | now + 500 millis   |
      | now + 1 week       | now - 2 months     |
    Then the mapped value is
      """
      [DateValues[localDate=null, localDateTime=2024-01-31T12:00, offsetDateTime=2024-01-31T09:30+02:00], DateValues[localDate=null, localDateTime=2024-01-31T10:00:15, offsetDateTime=2024-01-31T10:00:00.500+02:00], DateValues[localDate=null, localDateTime=2024-02-07T10:00, offsetDateTime=2023-11-30T10:00+02:00]]
      """
