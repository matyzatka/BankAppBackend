[![Build and Tests](https://github.com/matyzatka/BankAppBackend/actions/workflows/workflow.yml/badge.svg)](https://github.com/matyzatka/BankAppBackend/actions/workflows/workflow.yml) <img src="https://img.shields.io/github/deployments/matyzatka/BankAppBackend/matyzatka-bank-app-backend?style=plastic"> <img alt="Security Headers" src="https://img.shields.io/security-headers?style=plastic&url=https%3A%2F%2Fmatyzatka-bank-app-backend.herokuapp.com%2F"> <img src="https://img.shields.io/github/languages/top/matyzatka/BankAppBackend?style=plastic"> 

# BankAppBackend

A free-time project trying to simulate a banking application, with basic security using JWT, providing customer account
management, including related financial products and basic transactions.

The application communicates with the front-end using JSON objects through REST endpoints and offers an API that is used
to manage user accounts by an authorized person (having "ROLE_ADMIN"). <br>There is an option to register for new
clients and existing clients can use login. JWT is used to enter the client zone, and it allows viewing the client's
data, changing personal information, establishing or canceling a financial product (such as a savings account,
increasing your deposits faster than any other in the world) or sending money from your own account to another .
The possibility of depositing or withdrawing cash is a matter of course.
