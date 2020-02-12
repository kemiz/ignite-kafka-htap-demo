# HTAP Demo

Simple demo simulating sports bet placement. Ignite handles the bet transactions delivered via Kafka and Ignite SQL and KStreams are used for real-time analytics.
 
## Running the demo

### Compile
In the project directory run: 
  `mvn clean compile assembly:single`

### Run
In the scripts folder:

 - Start Confluent / Kafka platform
 - Start ignite node
 - Load initial data
 - Start ignite consumers
 - Start bet stream generator
 - Start total count per market 
 - Run any of the top3 scripts
