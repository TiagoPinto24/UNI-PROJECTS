# SoftEng_project
Repository of the project related to the Software Engeneering curricular unit

# Docker-compose.yaml structure

This file is divided into 2 main parts:

DB container - Starting by defining the image and the name of the container, followed by defining that the container should automatically restart if stopped unexpectedly.

Next up, we define the variables according to what's in the application.properties files. After that, we define the ports that the container should use, and we define the volume to make our data persistent even in case of the container stopping. The last part, the healthcheck is used to assure that the database container is running well and is useful to tell the app container when it is able to start.

App container - In the app container, we start by defining its name and ports, followed by the information necessary to connect successfully with the database and finishing by telling the container that it only initializes after the DB container is running and healthy, using the previously mentioned healthcare part.

# How to run

To run the application locally, firstly, we have to package the project using mvn package. After that we can just run the app using docker-compose up --build the first time we run it to build the container images and just docker-compose up afterwards.
