# !/bin/bash
if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi

kill $(sudo lsof -t -i:8080) # kill spring backend
kill $(sudo lsof -t -i:3000) # kill react frontend
sudo /etc/init.d/redis-server stop # stop redis
sudo /usr/local/kafka_2.11-2.4.1/bin/zookeeper-server-stop.sh # stop zookeeper
sudo /usr/local/kafka_2.11-2.4.1/bin/kafka-server-stop.sh # stop kafka

# build: npm run build
cd frontend && serve build -p 3000 &
nohup java -Xmx128M -Xms128M -jar springbackend/target/springbackend-0.0.1-SNAPSHOT.jar  > logs/springbackend-logs &
sudo /etc/init.d/redis-server start > logs/redis-logs &
nohup /usr/local/kafka_2.11-2.4.1/bin/zookeeper-server-start.sh /usr/local/kafka_2.11-2.4.1/config/zookeeper.properties > logs/zookeeper-logs &
nohup /usr/local/kafka_2.11-2.4.1/bin/kafka-server-start.sh /usr/local/kafka_2.11-2.4.1/config/server.properties > logs/kafka-logs &
