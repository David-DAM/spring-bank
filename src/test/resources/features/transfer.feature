Feature: Transference

  @transfer-success
  Scenario: Client makes call to POST /api/v1/transaction
    Given An account with 2000 euros
    When I transfer 100 euros to another
    Then The final amount is 1900 euros

  @transfer-duplicated
  Scenario: Client makes call to POST /api/v1/transaction twice by error
    Given An account with 2000 euros
    When I transfer 100 euros to another twice by slow internet
    Then The final amount is 1900 euros

  @transfer-account-not-found
  Scenario: Client makes call to POST /api/v1/transaction to unknown account
    Given An account with 2000 euros
    When I try to transfer 100 euros to non existent account
    Then I got an error and my transaction is failed

  @transfer-concurrent
  Scenario: Client makes call to POST /api/v1/transaction two times very quickly
    Given An account with 2000 euros
    When I try to transfer 100 euros twice in different transactions very quickly
    Then The final amount is 1800 euros