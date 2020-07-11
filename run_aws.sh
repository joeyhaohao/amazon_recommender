# !/bin/bash
# run in AWS EC2
if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi

nohup redis-server > logs/redis-logs &
nohup /home/ubuntu/kafka_2.11-2.4.1/bin/zookeeper-server-start.sh kafka_2.11-2.4.1/config/zookeeper.properties > logs/zookeeper-logs &
nohup /home/ubuntu/kafka_2.11-2.4.1/bin/kafka-server-start.sh kafka_2.11-2.4.1/config/server.properties > logs/kafka-logs &
nohup java -jar springbackend/target/springbackend-0.0.1-SNAPSHOT.jar > logs/springbackend-logs &
nohup java -jar recommender/target/realtime-recommender-jar-with-dependencies.jar > logs/recommender-logs &
cd amazon-frontend && npm run build && serve build -p 3000 &
