INSERT INTO accounts (id, version, iban, balance_in_cents, status, created_at)
VALUES ('AC-1', 1, 'ES3601520826', 200000, 'ACTIVE', now());

INSERT INTO accounts (id, version, iban, balance_in_cents, status, created_at)
VALUES ('AC-2', 1, 'ES3601520827', 200000, 'ACTIVE', now());

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES ('LE-1', 'TR-1', 'ES3601520826', 200000, 'CREDIT', now());

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES ('LE-2', 'TR-1', 'ES3601520830', 200000, 'DEBIT', now());