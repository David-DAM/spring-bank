Feature: Transference

  Scenario: Client makes call to POST /api/v1/transaction
    Given An account with 100 euros
    When I transfer 40 euros to another
    Then The final amount is 60 euros