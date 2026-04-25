#!/bin/bash


echo "Starting app..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=load &
PID=$!

echo "Waiting for app..."
until curl -s http://localhost:8080/actuator/health | grep UP > /dev/null; do
  sleep 2
done

echo "Running load test..."
./mvnw gatling:test

echo "Stopping app..."
kill $PID