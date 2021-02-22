
# RESTful Weather application
## Tech Stack:
* Java 11
* SpringBoot
* Lombok
* Spring Data JPA
* H2 in-memory DB
* Mockito
* MockMvc
* Wiremock
* Flyway

## Topics
1. [How to run this application](#How-to-run-this-application)
2. [How to access the spring boot restful application](#How-to-access-the-spring-boot-restful-application)
3. [Design and implementation](#Design-and-implementation)
4. [Assumptions](#Assumptions)

## How to run this application

* Navigate the root folder /weather-app under the command line
* Run the command to build the whole project:
 `gradle clean build`
* Run the command to start the application: 
  `java  -jar ./build/libs/weather-app-0.0.1-SNAPSHOT.jar --openweather.api.token={your openweather api token}`
* If you want to run in IDE, please check application.yml to change "FIXME" in token part

## How to access the spring boot restful application
you can access via:
`http://localhost:8090/weather?city={city}&country={two characters country code}&token={can be found in data init}`

This is the example:
`http://localhost:8090/weather?city=shanghai&country=cn&token=ba3a5f28-eddf-43ee-8202-88682f23858e`

## Design and implementation
* Implement a separate RateLimitService for rate limitation, using a simple way like sliding window to count the requests for last hour. For calculating, stored requests history as token usages into a table.
* Hibernate builds the entity layer
* Flyway prepares the initial data
* Spring Data JPA builds the repository layer
* Mockito and MockMvc unit test service and controller layer, Wiremock for 3rd party API tests
* Defined customised exceptions for better error handling and all the exceptions can be centrally handled in one place (ControllerExceptionHandler.java)
* Lombok automatically generates getter,setter, constructor, hashcode, log etc.
* High test coverage

## Assumptions
* didn't consider rate limitation in Openweather API side
* since the overall traffic is pretty low, didn't choose 3rd party lib for rate limits and faster ways for storing requests history,
