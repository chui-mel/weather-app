
# RESTful Weather application
## Tech Stack:
* SpringBoot
* Lombok
* Spring Data JPA
* H2 in-memory DB
* Mockito
* MockMvc
* Flyway

## Topics
1. [How to run this application](#How-to-run-this-application)
2. [How to access the spring boot restful application](#How-to-access-the-spring-boot-restful-application)
3. [Design](#Design)
4. [Advantages of this application](#Advantages-of-this-application)

## How to run this application

* Navigate the root folder /weather-app under the command line
* Run the command to build the whole project: **gradle clean build**
* Run the command to start the application: **java -jar ./build/libs/weather-app-0.0.1-SNAPSHOT.jar**

## How to access the spring boot restful application

## Design 

## Advantages of this application
* Hibernate builds the entity layer
* Flyway prepares the initial data
* Spring Data JPA builds the repository layer & H2 in-mem database used as Unit Test to test this layer
* Mockito and MockMvc unit test service and controller layer
* All the exceptions can be centrally handled in one place (ControllerExceptionHandler.java)
* Lombok automatically generates getter,setter, constructor, hashcode, log etc.
