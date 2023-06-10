-- Creating the transactions table
CREATE TABLE transactions (
    id                       INT IDENTITY (1,1) PRIMARY KEY,
    sender_account_number    VARCHAR(30)    NOT NULL,
    recipient_account_number VARCHAR(30)    NOT NULL,
    amount                   DECIMAL(10, 2) NOT NULL,
    description              VARCHAR(200),
    transaction_date         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
);

-- Creating the clients table
CREATE TABLE clients (
    id             INT IDENTITY (1,1) PRIMARY KEY,
    first_name     VARCHAR(100) NOT NULL,
    second_name    VARCHAR(100),
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(100) NOT NULL,
    date_of_birth  DATE         NOT NULL,
    place_of_birth VARCHAR(100) NOT NULL,
    gender         VARCHAR(10)  NOT NULL,
    user_id        INT          NOT NULL
);