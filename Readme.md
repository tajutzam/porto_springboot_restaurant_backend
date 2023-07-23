# Getting Started

#### Please Open with Intellij idea for better experience

# Step to run this application
 - Prerequisites: Ensure that you have obtained the Midtrans server key, which is required for processing transactions.
 - Configuration: Update the application.properties file with the necessary Midtrans configuration, including the server key
 - Hosting the Application: Deploy the application to your desired hosting service. If you are in development mode, you can use tools like ngrok to expose your local application to the internet temporarily for testing purposes.
 - Setting Up Midtrans Notification: In the Midtrans dashboard, configure the notification URL to point to your hosted application's /api/transaction endpoint. This endpoint should handle incoming transaction notifications from Midtrans.
 - Testing: Once everything is set up, you can perform test transactions to see how the application handles Midtrans notifications and updates the transaction status accordingly.


### Roles
- User 
- Restaurant
- Admin

### Api spec
- User 
  - cart [file:///tmp/swagger-ui8/index.html](file:///tmp/swagger-ui8/index.html)
  - menu [file:///tmp/swagger-ui11/index.html](file:///tmp/swagger-ui11/index.html)
  - transaction [file:///tmp/swagger-ui9/index.html](file:///tmp/swagger-ui9/index.html)
  - user [file:///tmp/swagger-ui5/index.html](file:///tmp/swagger-ui5/index.html)
- restaurant 
  - menu [file:///tmp/swagger-ui4/index.html](file:///tmp/swagger-ui4/index.html)
  - restaurant [file:///tmp/swagger-ui3/index.html](file:///tmp/swagger-ui3/index.html)
  - transaction [file:///tmp/swagger-ui10/index.html](file:///tmp/swagger-ui10/index.html)
- admin 
  - [file:///tmp/swagger-ui7/index.html](file:///tmp/swagger-ui4/index.html)

# Authorization 
- use the access token and refresh the jwt token as the authorization, send your token through the Bearer token authentication



### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#web)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Validation](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#io.validation)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

