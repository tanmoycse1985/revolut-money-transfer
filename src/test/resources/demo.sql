--!!! This script is used for unit test cases, DO NOT CHANGE!

DROP TABLE IF EXISTS User;

CREATE TABLE User (UserId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
 UserName VARCHAR(30) NOT NULL,
 EmailAddress VARCHAR(30) NOT NULL);

CREATE UNIQUE INDEX idx_ue on User(UserName,EmailAddress);

INSERT INTO User (UserName, EmailAddress) VALUES ('test1','test001@gmail.com');
INSERT INTO User (UserName, EmailAddress) VALUES ('test2','test002@gmail.com');
INSERT INTO User (UserName, EmailAddress) VALUES ('test3','test003@gmail.com');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30),
Balance DECIMAL(19,4),
CurrencyCode VARCHAR(30)
);

CREATE UNIQUE INDEX idx_acc on Account(UserName,CurrencyCode);

INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',100.0000,'USD');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',200.0000,'USD');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',500.0000,'EUR');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',500.0000,'EUR');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',500.0000,'GBP');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',500.0000,'GBP');
