#!/usr/bin/env sh

./gradlew clean grpc:buildImage

cd grpc

docker-compose up --build