#!/bin/bash

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8080
else
  echo "No WAS is connected to nginx" >> /home/ec2-user/ninetosix-api/deploy.log
  exit 1
fi

echo "Start health check of WAS at 'http://127.0.0.1:${TARGET_PORT}' ..." >> /home/ec2-user/ninetosix-api/deploy.log

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
  echo "#${RETRY_COUNT} trying..." >> /home/ec2-user/ninetosix-api/deploy.log
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/swagger-ui.html)

  if [ ${RESPONSE_CODE} -eq 200 ]; then
    echo "New WAS successfully running" >> /home/ec2-user/ninetosix-api/deploy.log
    exit 0
  elif [ ${RETRY_COUNT} -eq 10 ]; then
    echo "Health check failed." >> /home/ec2-user/ninetosix-api/deploy.log
    exit 1
  fi
  sleep 10
done