#!/usr/bin/env sh

./gradlew clean grpc:buildImage

cd rest_gateway

./gradlew buildImage

cd ..

cd grpc

docker-compose up --build -d