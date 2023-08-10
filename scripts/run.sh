#!/bin/bash
echo "#################################################################################################" >> /home/ec2-user/ninetosix-api/deploy.log
echo "data now : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/ninetosix-api/deploy.log

BUILD_JAR=$(ls /home/ec2-user/ninetosix-api/build/libs/*SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/ninetosix-api/deploy.log

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8080
else
  echo "[$NOW_TIME] No WAS is connected to nginx" >> /home/ec2-user/ninetosix-api/deploy.log
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "Kill WAS running at ${TARGET_PORT}." >> /home/ec2-user/ninetosix-api/deploy.log
  sudo kill ${TARGET_PID}
fi

echo "> build 파일 복사" >> /home/ec2-user/ninetosix-api/deploy.log
DEPLOY_PATH=/home/ec2-user/ninetosix-api/
cp $BUILD_JAR $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/ninetosix-api/deploy.log
echo "> DEPLOY_JAR: $DEPLOY_JAR"    >> /home/ec2-user/ninetosix-api/deploy.log
source /home/ec2-user/ninetosix-api/env.yml
echo "java -jar -Dserver.port=${TARGET_PORT} \
         -Dmail.username=$MAIL_USERNAME \
         -Dmail.password=$MAIL_PASSWORD \
         -Djwt.key=$JWT_SECRET_KEY \
         -Ddb.url=$DB_URL \
         -Ddb.username=$DB_USERNAME \
         -Ddb.password=$DB_PASSWORD \
         $DEPLOY_JAR"   >> /home/ec2-user/ninetosix-api/deploy.log
echo "#################################################################################################" >> /home/ec2-user/ninetosix-api/deploy.log

LOG_DIR=/home/ec2-user/ninetosix-api/logs
nohup java -jar -Dserver.port=${TARGET_PORT} \
   -Dmail.username=$MAIL_USERNAME \
   -Dmail.password=$MAIL_PASSWORD \
   -Djwt.key=$JWT_SECRET_KEY \
   -Ddb.url=$DB_URL \
   -Ddb.username=$DB_USERNAME \
   -Ddb.password=$DB_PASSWORD \
   $DEPLOY_JAR >> $LOG_DIR/deploy.log 2> /home/ec2-user/ninetosix-api/deploy_err.log &