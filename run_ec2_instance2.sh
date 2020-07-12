# !/bin/bash

if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi

sudo /etc/init.d/redis-server stop # stop redis
sudo /usr/local/kafka_2.11-2.4.1/bin/zookeeper-server-stop.sh # stop zookeeper
sudo /usr/local/kafka_2.11-2.4.1/bin/kafka-server-stop.sh # stop kafka

sudo /etc/init.d/redis-server start > logs/redis-logs &
nohup /usr/local/kafka_2.11-2.4.1/bin/zookeeper-server-start.sh /usr/local/kafka_2.11-2.4.1/config/zookeeper.properties > logs/zookeeper-logs &
nohup /usr/local/kafka_2.11-2.4.1/bin/kafka-server-start.sh /usr/local/kafka_2.11-2.4.1/config/server.properties > logs/kafka-logs &
nohup java -jar recommender/target/realtime-recommender-jar-with-dependencies.jar > logs/recommender-logs &
