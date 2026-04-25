Feature: Transference

  @transfer-success
  Scenario: Client makes call to POST /api/v1/transaction
    Given An account with 2000 euros
    When I transfer 100 euros to another
    Then The final amount is 1900 euros