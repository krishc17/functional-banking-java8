CREATE SCHEMA IF NOT EXISTS banking;

SET SCHEMA banking;

DROP TABLE IF EXISTS banking.accounts;

CREATE TABLE banking.accounts (
  no           VARCHAR(256)   NOT NULL PRIMARY KEY,
  name         VARCHAR(256)   NOT NULL,
  balance      DECIMAL  NOT NULL,
  opening_date TIMESTAMP NOT NULL,
  closing_date TIMESTAMP
);