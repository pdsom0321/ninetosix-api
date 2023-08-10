#!/bin/bash

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "Nginx currently proxies to ${CURRENT_PORT}." >> /home/ec2-user/ninetosix-api/deploy.log

if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
elif [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8080
else
  echo "No WAS is connected to nginx" >> /home/ec2-user/ninetosix-api/deploy.log
  exit 1
fi

echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc >> /home/ec2-user/ninetosix-api/deploy.log

echo "Now Nginx proxies to ${TARGET_PORT}." >> /home/ec2-user/ninetosix-api/deploy.log
sudo systemctl reload nginx

echo "Nginx reloaded." >> /home/ec2-user/ninetosix-api/deploy.log