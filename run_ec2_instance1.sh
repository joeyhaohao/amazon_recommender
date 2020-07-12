# !/bin/bash
if [ ! -d "./logs/" ]
then
  mkdir ./logs/
fi

kill $(sudo lsof -t -i:8080) # kill spring backend
kill $(sudo lsof -t -i:3000) # kill react frontend

cd amazon-frontend && npm run build && serve build -p 3000 &
nohup java -jar springbackend/target/springbackend-0.0.1-SNAPSHOT.jar  > logs/springbackend-logs &
