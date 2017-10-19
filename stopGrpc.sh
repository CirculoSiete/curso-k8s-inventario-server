#!/usr/bin/env sh

cd grpc

docker-compose stop
docker-compose kill
docker-compose rm -f