# Spring MVC with STOMP over Websockets

## Running App 
You can run this demo application as is without any external dependencies by Running the main class in StompDemoApplication.kt
Execute `mvn spring-boot:run` to compile and start the project (from the root project directory) 

### Alternative Run setup
Alternatively you  can start this up with a Rabbit MQ as the Stomp Relay Message broker by setting the `destination.stomp.simplebroker=false` and configuring the rabbitmq broker details.


## Using the App
- Browse to `http://localhost:8080` to open the chat sample.
- Type in a name to be your username.
- Click the Connect button.
- Browse to `http://localhost:8080/send_test` in a separate window and you'll see a chat message appear in your chat sample application.

You can also open `http://localhost:8080` in multiple browsers and send messages directly.

#Implementation Details

- Front End is using StompJS over SockJS.
- There is a reconnect strategy when disconnects to the server are detected.
- Chat Controller is Using Springboot and Springboot starter for ampq (for Stomp).
- Chat Sessions are stored in a singleton service as an example of tracking users sessions and state.
- Chat messages are stored in a service as an example of a data store (it is just storage in this demo but could be use to give users messages if they re-connect).
- For getting existing chat messages a timestamp should be used to track client data sets 'state' in relation to servers available data.

#### A note for production implementations DONT use the simple broker