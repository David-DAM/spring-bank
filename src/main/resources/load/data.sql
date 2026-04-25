INSERT INTO accounts (id, iban, status, created_at)
VALUES ('AC-1', 'ES3601520826', 'ACTIVE', now());

INSERT INTO accounts (id, iban, status, created_at)
VALUES ('AC-2', 'ES3601520827', 'ACTIVE', now());

INSERT INTO accounts (id, iban, status, created_at)
VALUES ('AC-3', 'ES3601520828', 'ACTIVE', now());

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES ('LE-1', 'TR-1', 'ES3601520826', 2000, 'DEBIT', now());

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES ('LE-2', 'TR-2', 'ES3601520827', 2000, 'DEBIT', now());

INSERT INTO ledger_entries (id, transaction_id, account_id, amount, type, created_at)
VALUES ('LE-3', 'TR-3', 'ES3601520828', 2000, 'DEBIT', now());