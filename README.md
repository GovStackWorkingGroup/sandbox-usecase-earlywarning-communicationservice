# Sandbox use case - Early Warning
# Communication Service
Microservice which is responsible for handling communication to the end users, recipients of the broadcast.
Broadcasts published from the threat service are delivered, via Information Mediator, to the communication service for further processing.
This means getting recipients and their data from the user service based on the location and other relevant data from the broadcast.
In this sandbox demo, this background process is shown using log service. 
The logs are created and published to the Information Mediator, and then presented to the user on the UI.
For testing the Early Warning system, check the user service for credentials.

# Tech stack
Microservice is using the following: <br>
Java 17, Spring Boot, Kafka pub/sub, Docker/Helm charts for deployment.

# Note
In order to test the Early Warning system, make sure to run the rest of the services and frontend.