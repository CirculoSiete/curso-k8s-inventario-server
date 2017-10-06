package com.circulosiete.cursos.k8s.warehouse;

import org.grpc.spring.boot.autoconfigure.annotation.EnableGRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableGRpcServer
public class InventarioServer {
  public static void main(String[] args) {
    SpringApplication.run(InventarioServer.class, args);
  }
}
