# Online Betting Demo

Simple demo that demonstrates an online betting app performing both transactions analytics.

 - Ignite is used as the transactional store and SQL analytics
 - Kafka is used as the messaging layer and KStreams for analytics
 
## Running the demo

### Compile
In the project directory run: 
  `mvn clean compile assembly:single`

### Run
In the scripts folder:

 - Start Confluent / Kafka platform
 - Start ignite node
 - Start ignite consumers
 - Start bet stream generator
 - Start total count per market 
 - Run top3 scripts
