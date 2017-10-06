package com.circulosiete.cursos.k8s.warehouse;

import org.grpc.spring.boot.autoconfigure.annotation.EnableGRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableGRpcServer
public class WarehouseApplication {
  public static void main(String[] args) {
    SpringApplication.run(WarehouseApplication.class, args);
  }
}
