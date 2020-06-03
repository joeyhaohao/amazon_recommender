# local
if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi
nohup redis-server > ./logs/redis-logs &
nohup /usr/local/Cellar/kafka/2.4.1/bin/zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties > ./logs/zookeeper-logs &
nohup /usr/local/Cellar/kafka/2.4.1/bin/kafka-server-start /usr/local/Cellar/kafka/2.4.1/libexec/config/server.properties > ./logs/kafka-logs &

# Amazon EC2
