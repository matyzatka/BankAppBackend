[![Build and Tests](https://github.com/matyzatka/BankAppBackend/actions/workflows/workflow.yml/badge.svg)](https://github.com/matyzatka/BankAppBackend/actions/workflows/workflow.yml) <img src="https://img.shields.io/github/deployments/matyzatka/BankAppBackend/matyzatka-bank-app-backend?style=plastic"> <img alt="Security Headers" src="https://img.shields.io/security-headers?style=plastic&url=https%3A%2F%2Fmatyzatka-bank-app-backend.herokuapp.com%2F"> <img src="https://img.shields.io/github/languages/top/matyzatka/BankAppBackend?style=plastic"> 

# Banking application - Backend

A free-time project trying to simulate a banking application, with basic security using JWT, providing customer account
management, including related financial products and simple, but secure transactions.

The application communicates through REST endpoints and offers an API, used
to manage user accounts by authorized person. <br>There is an option to register for new
clients and existing clients can use login. JWT is used to enter the client zone, and allows viewing the client's
data, changing personal information, establishing or canceling a financial product (such as a savings account or credit card) or sending money from your account to another. Actual currency values are provided by Retrofit calls.
The possibility of depositing or withdrawing cash is a matter of course, but that was only the beginning.

## Table of Contents
- [Used technologies](#used-technologies)
- [Features](#features)
- [How to use app](#how-to-use-app)
- [Demo](#demo)

## Used technologies
- **Project**: Java 18, Spring Boot 2.7.3, Gradle 7.4
- **Security**: Spring Security, OAuth JWT
- **Persistence**: Spring JPA, MySQL, H2, Hibernate 
- **Logging**: Logback/Sl4fj
- **Testing**: JUnit 5, MockMvc, Awaitility, Spring Security Test, Jacoco
- **Other**: Spring Validation, Spring Mail, Lombok, Retrofit, Dotenv
- **CI**: Checkstyle, Build and Tests - Github Actions
- **CD**: Heroku Cloud with JawsDB

## Features
- Secured endpoints
- Secured HTTP headers
- Password encryption
- JWT token used for authorization, using role-based authentication for Customer API
- Registration, Login, Confirm registration by email
- **Customer**: is enabled after confirmation of registration, personal info accessible only to admin or customer himself, can have roles USER, ADMIN
- **Customer's Account**: has list of financial products, can be blocked/unblocked (disables ATM + client-zone endpoints except "/unblock")
- **Products**: by default in EUR, unique IBAN, can have interest rates, cards are secured by PIN code, balance holded by BigDecimal 
  - Checking account
	  - is present in account by default
	  - is synchronized with debit card
  - Debit Card 
	  - is present in account by default
	  - is synchronized with checking account
  - Savings Account
	  - periodally increases amount of saved money, depending on the value of interest rate (each 10 seconds)
  - Credit Card
    - can be used only for payments, not working with ATM
  
 - **Client-zone**: only for JWT authorized customers, offers several options for checking and managing client's data and products.

    - show all customer personal data and products (possible in 100+ currencies with actual rates)
    - update customers personal data
    - make a transaction
    - show transaction history
    - block/unblock customer account
    - delete customer account
    - add a product to customer's account
    - delete a product from account
  
 - **Customer API**: provides basic operations with customers to admins
    - show all customers and their personal data + products
    - get customer
    - update customer
    - delete customer
    
 - **ATM**: works only with debit cards, requires valid PIN code
    - deposit cash: customer can deposit money to whichever product he owns
    - withdraw cash: handles insufficient funds
    
 - **Custom Exceptions**: majority of exceptions is handled by CustomExceptionHandler, displaying human-friendly messages
 
 ## How to use app
 
 ## Demo
