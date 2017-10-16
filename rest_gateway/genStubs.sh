#!/usr/bin/env sh

# Generate gRPC Stub, Gateway and Swagger
docker run --rm -v $(pwd)/warehouse:$(pwd) -w $(pwd) znly/protoc --go_out=plugins=grpc:. --grpc-gateway_out=logtostderr=true:. --swagger_out=logtostderr=true:. -I. WarehouseService.proto  
