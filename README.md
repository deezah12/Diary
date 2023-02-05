Introduction

Welcome to the diary webapp server-side using springboot, maven and mysql database!
This application allows users to create, read, update and delete their daily diary entries.

Technology Stack

Spring Boot: A Java-based framework used for building web applications
Maven: A build automation tool used for managing project dependencies and building the application
MySQL: A popular open-source relational database management system

Prerequisites

Java Development Kit (JDK)
Maven
MySQL

Usage
The webapp allows you to perform the following actions:

Create a diary entry
View all diary entries
Update a diary entry
Delete a diary entry

Setup

Clone the repository: git clone https://github.com/<your-username>/diary-webapp.git
Navigate to the project directory: cd diary-webapp
Build the project using maven: mvn clean install
Create a database named diary in MySQL and update the database credentials in src/main/resources/application.properties
Run the application using spring boot: mvn spring-boot:run
The application will be available at http://localhost:8080/

Database Configuration

The application uses a MySQL database. You can configure the database by modifying the
application.properties file located in the src/main/resources directory.
mysql -u root -p
CREATE DATABASE diary_db;
USE diary_db;
CREATE TABLE entries (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL
);


Conclusion

With these steps, you should be able to run and use the diary webapp. 
If you encounter any issues or have questions, please open an issue in the repository.

Contributing

If you wish to contribute to the project, please follow the guidelines in the CONTRIBUTING.md file.

