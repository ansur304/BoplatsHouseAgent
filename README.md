# BoplatsHouseAgent
A Spring Boot Application I have been using to login and apply for houses in https://boplats.se housing site in Sweden based on user preferences.
It's been implemented with below services
 -> Simple GUI to input the user details. Ex: Personal Number/email , password and trigger the job
 -> Service to simulate user login on to https://boplats.se and search the housed based on preferences and register interest. Using    Selenium and Webdriver
 -> Service to generate a Log file with all the registered houses by the job and trigger an email to the registered email address with the logger file
 
 Tools/Frameworks:
 Spring Boot Starter project with depemdency
 J2EE, Java8
 REST Web service
 Java Mail API
 Maven
 MongoDB on localHost or AWS Instance
 
 TODO:
 Implement it in Microservice architecture
 Deploy the WAR file into AWS EC2 instance and run as a service to any new customers
