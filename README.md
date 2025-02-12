# Selenium Vs Playwright

## Overview
This project compares Selenium and Playwright for web automation using Maven and Java. It includes test cases for performance, reliability, and ease of use evaluation.

## Table of Contents
1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
   - [Selenium Setup](#selenium-setup)
   - [Playwright Setup](#playwright-setup)
4. [Running Tests](#running-tests)
5. [Comparison Metrics](#comparison-metrics)
6. [Project Structure](#project-structure)
7. [Architecture of the Application](#architecture-of-the-application)
8. [Conclusion](#conclusion)
9. [Contributions](#contributions)

## Features
- Setup and installation guides for Selenium and Playwright.
- Sample test scripts for common automation scenarios.
- Performance benchmarking.
- Comparisons on speed, stability, and cross-browser support.
- Handling of modern web features like shadow DOM, iframes, and network interception.

## Prerequisites
Ensure you have the following installed:
- Java (>=8)
- Maven
- Web browsers (Chrome, Firefox, Edge)

## Installation

### Selenium Setup
1. Add Selenium dependencies to `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.seleniumhq.selenium</groupId>
       <artifactId>selenium-java</artifactId>
       <version>4.x.x</version>
   </dependency>
   ```
2. Install WebDriver (ChromeDriver, GeckoDriver, etc.).
3. Verify installation by running a test script.

### Playwright Setup
1. Add Playwright dependencies to `pom.xml`:
   ```xml
   <dependency>
       <groupId>com.microsoft.playwright</groupId>
       <artifactId>playwright</artifactId>
       <version>1.x.x</version>
   </dependency>
   ```
2. Install Playwright dependencies:
   ```bash
   mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
   ```
3. Verify installation by running a test script.

## Running Tests
All tests for both Selenium and Playwright are executed using `App.java`. Run the following command:
```bash
mvn exec:java -Dexec.mainClass=com.project.SeleniumVsPlayWright.App
```

## Comparison Metrics
This project evaluates the following aspects:
1. **Performance:** Execution time of test scripts.
2. **Ease of Setup:** Installation and configuration complexity.
3. **Browser Support:** Cross-browser testing capabilities.
4. **Reliability:** Consistency and stability of test execution.
5. **Modern Web Features:** Handling of new web technologies.

## Project Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── seleniumTests (contains Selenium test cases)
│   │   │   ├── playwrightTests (contains Playwright test cases)
│   │   │   ├── App.java (core class for executing tests in both Playwright and Selenium)
│   │   ├── resources (contains configuration files)
│   ├── test
│   │   ├── java
│   │   │   ├── seleniumTests (contains Selenium test scripts)
│   │   │   ├── playwrightTests (contains Playwright test scripts)
├── pom.xml (Maven project configuration)
├── README.md (Project documentation)
├── .gitignore (Git ignore file)
```
## Architecture of the Application
The core of this project lies in an application that manages the execution of the test cases across 
both Selenium and Playwright. The architecture includes three main components:
1. **App Class**: This orchestrator controls the execution of tests in both Selenium and 
Playwright. It runs the tests, calculates the execution time, and compares the results.
2. **Selenium Testing Class**: This class contains the logic for running the test with Selenium. 
It interacts with the web elements, performs the necessary actions, and verifies the 
results.
3. **Playwright Testing Class**: Similar to the Selenium Testing class, this class runs the same 
test case using Playwright, utilizing Playwright’s API to interact with the web 
application.

![image](https://github.com/user-attachments/assets/efc822b7-0258-42ad-a5cd-5c01e6156e08)
![image](https://github.com/user-attachments/assets/32c96789-eb0a-4122-9d12-aaa2aec17900)

## Conclusion
This project provides a practical comparison of Selenium and Playwright, helping users choose the best automation tool for their needs.

## Contributions
Contributions are welcome! Fork this repository and submit pull requests with improvements.

