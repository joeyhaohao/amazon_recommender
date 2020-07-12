# !/bin/bash

if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi


kill $(sudo lsof -t -i:7878) # kill recommender server

nohup java -Xmx512m -Xms512m -Dserver.port=7878 -jar recommender/target/realtime-recommender-jar-with-dependencies.jar > logs/recommender-logs &
