#!/bin/bash
echo "#################################################################################################" >> /home/ec2-user/ninetosix-api/deploy.log
echo "data now : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/ninetosix-api/deploy.log

BUILD_JAR=$(ls /home/ec2-user/ninetosix-api/build/libs/*SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/ninetosix-api/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/ninetosix-api/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/ninetosix-api/deploy.log
else
  echo "> kill -15 $CURRENT_PID" >> /home/ec2-user/ninetosix-api/deploy.log
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> build 파일 복사" >> /home/ec2-user/ninetosix-api/deploy.log
DEPLOY_PATH=/home/ec2-user/ninetosix-api/
cp $BUILD_JAR $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/ninetosix-api/deploy.log
echo "> DEPLOY_JAR: $DEPLOY_JAR"    >> /home/ec2-user/ninetosix-api/deploy.log
source /home/ec2-user/ninetosix-api/env.yml
echo "java -jar \
         -Dmail.username=$MAIL_USERNAME \
         -Dmail.password=$MAIL_PASSWORD \
         -Djwt.key=$JWT_SECRET_KEY \
         -Ddb.url=$DB_URL \
         -Ddb.username=$DB_USERNAME \
         -Ddb.password=$DB_PASSWORD \
         $DEPLOY_JAR"   >> /home/ec2-user/ninetosix-api/deploy.log
echo "#################################################################################################" >> /home/ec2-user/ninetosix-api/deploy.log

LOG_DIR=/home/ec2-user/ninetosix-api/logs
if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR
fi
nohup java -jar \
   -Dmail.username=$MAIL_USERNAME \
   -Dmail.password=$MAIL_PASSWORD \
   -Djwt.key=$JWT_SECRET_KEY \
   -Ddb.url=$DB_URL \
   -Ddb.username=$DB_USERNAME \
   -Ddb.password=$DB_PASSWORD \
   $DEPLOY_JAR >> $LOG_DIR/deploy.log 2> /home/ec2-user/ninetosix-api/deploy_err.log &
