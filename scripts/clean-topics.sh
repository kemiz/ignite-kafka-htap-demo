echo "Clearing topics"
/Users/kemi/Downloads/confluent-5.0.0-ent/bin/kafka-topics --zookeeper 127.0.0.1 --delete --topic Users
/Users/kemi/Downloads/confluent-5.0.0-ent/bin/kafka-topics --zookeeper 127.0.0.1 --delete --topic Bets
/Users/kemi/Downloads/confluent-5.0.0-ent/bin/kafka-topics --zookeeper 127.0.0.1 --delete --topic Markets
/Users/kemi/Downloads/confluent-5.0.0-ent/bin/kafka-topics --zookeeper 127.0.0.1 --delete --topic Bet-Market-Selection-Feed

