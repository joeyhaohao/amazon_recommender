# !/bin/bash
# run in local environment
if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi

nohup redis-server /usr/local/etc/redis.conf > logs/redis-logs &
nohup zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties > logs/zookeeper-logs &
nohup kafka-server-start /usr/local/etc/kafka/server.properties > logs/kafka-logs &
elasticsearch > logs/elasticsearch-logs &

cd frontend && serve build -p 3000 &
nohup java springbackend/target/springbackend-0.0.1-SNAPSHOT.jar  > logs/springbackend-logs &
nohup java -Dserver.port=7878 -jar recommender/target/realtime-recommender-jar-with-dependencies.jar > logs/recommender-logs &
