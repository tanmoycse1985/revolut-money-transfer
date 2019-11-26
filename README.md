# Revolut Money Transfer App
Java & RESTful API for money transfers between users accounts

##Technologies
JAX-RS API
Java
H2 in memory database
Log4j
Jetty Container
Apache HTTP Client


##How to run
mvn exec:java

Application starts a jetty server on localhost port 8080 An H2 in memory database initialized with some sample user and account data To view

##Available Services
HTTP METHOD	PATH								USAGE
GET	/user/{userName}							get user by user name
GET	/user/all									get all users
PUT	/user/create								create a new user
POST	/user/{userId}							update user
DELETE	/user/{userId}							remove user
GET	/account/{accountId}						get account by accountId
GET	/account/all								get all accounts
GET	/account/{accountId}/balance				get account balance by accountId
PUT	/account/create								create a new account
DELETE	/account/{accountId}					remove account by accountId
PUT	/account/{accountId}/withdraw/{amount}		withdraw money from account
PUT	/account/{accountId}/deposit/{amount}		deposit money to account
POST /transaction								perform transaction between 2 user accounts

##Sample JSON for User and Account
User :
{  
  "userName":"test1",
  "emailAddress":"test1@gmail.com"
} 
User Account: :
{  
   "userName":"test1",
   "balance":15.00,
   "currencyCode":"GBP"
} 
User Transaction:
{  
   "currencyCode":"EUR",
   "amount":1800.00,
   "fromAccountId":1,
   "toAccountId":2
}