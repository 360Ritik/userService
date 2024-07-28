

# Student Eligibility Service

This project provides a REST API for processing student records from CSV files to determine their eligibility for scholarships based on dynamic criteria. The project uses Spring Boot, H2 in-memory database, OpenCSV for CSV processing, and SpringDoc for API documentation.


## [Watch API Demonstration Video]https://github.com/user-attachments/assets/f92c427d-e850-4305-9170-a30906d6ff9e




## Features

- Upload and process CSV files containing student records.
- Determine eligibility based on dynamic criteria.
- Search for a student's eligibility status by roll number.
- Java 21 Virtual Threads, support for faster CSV processing.
- In-memory H2 database for quick data access.
- Swagger UI for API documentation and testing.

## Prerequisites

- Java 21 or higher
- Maven

## Getting Started

### Build the Project
- mvn clean install

### Run the Application
- mvn spring-boot:run

### Access H2 Console
The H2 console can be accessed at: http://localhost:8080/h2-console

### Access Swagger UI
The Swagger UI can be accessed at: http://localhost:8080/swagger-ui.html

### Clone the Repository

```sh
git clone https://github.com/360Ritik/userService.git
cd studentService
