USE pos_blockchain_db;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tx;
DROP TABLE IF EXISTS purchase_history;
DROP TABLE IF EXISTS block;
DROP TABLE IF EXISTS pending_tx;

CREATE TABLE users
(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  public_key VARCHAR(1000) NOT NULL,
  private_key VARCHAR(1000) NOT NULL,
  node_address INT(1) NOT NULL,
  is_administrator BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE tx
(
  hash VARBINARY(255) NOT NULL PRIMARY KEY,
  sender_id INT NOT NULL,
  recipient_id INT NOT NULL,
  amount INT NOT NULL,
  timestamp DATETIME NOT NULL
);

CREATE TABLE purchase_history
(
  hash VARBINARY(255) NOT NULL,
  product_id INT NOT NULL,
  name VARCHAR(20) NOT NULL,
  price INT NOT NULL,
  count INT NOT NULL
);

CREATE TABLE block
(
  hash VARBINARY(255),
  block_index INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  previous_block_hash VARBINARY(255),
  nonce INT NOT NULL,
  timestamp DATETIME NOT NULL
);

CREATE TABLE pending_tx
(
  block_index INT NOT NULL,
  seq INT(1) NOT NULL,
  tx_hash VARBINARY(255) NOT NULL,
  PRIMARY KEY (block_index, seq)
);
