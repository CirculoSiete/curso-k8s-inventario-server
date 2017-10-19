#!/usr/bin/env sh

./gradlew clean grpc:buildImage validation:buildImage

cd rest_gateway

./gradlew buildImage

cd ../grpc

docker-compose up --build -d