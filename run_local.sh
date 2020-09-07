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
