-- Creating the branch table
CREATE TABLE branches (
    id      INT IDENTITY(1,1) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL
);

-- Creating the users table
CREATE TABLE users (
    id        INT IDENTITY(1,1) PRIMARY KEY,
    login     VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    role      VARCHAR(50)  NOT NULL,
    branch_id INT,
    FOREIGN KEY (branch_id) REFERENCES branches(id)
);

-- Creating accounts table
CREATE TABLE accounts (
    id             INT IDENTITY(1,1) PRIMARY KEY,
    user_id        INT            NOT NULL,
    account_number VARCHAR(20)    NOT NULL UNIQUE,
    balance        DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);