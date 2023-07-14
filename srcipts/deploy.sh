#!/bin/bash
REPOSITORY=/home/ec2-user/ninetosix-api/
PROJECT_NAME=ninetosix-api
echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

# 수행 중인 애플리케이션 프로세스 ID => 구동 중이면 종료하기 위함
CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep jar | awk '{print $1}')

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME # Jar 파일은 실행 권한이 없는 상태이므로 권한 부여

echo "> $JAR_NAME 실행"
nohup java -jar \
   -Dmail.username=$MAIL_USERNAME \
   -Dmail.password=$MAIL_PASSWORD \
   -Djwt.key=JWT_SECRET_KEY \
   -Ddb.url=$DB_URL \
   -Ddb.username=$DB_USERNAME \
   -Ddb.password=$DB_PASSWORD \
   $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &