package com.circulosiete.cursos.k8s.server;

import org.springframework.boot.SpringApplication;
import org.grpc.spring.boot.autoconfigure.annotation.EnableGRpcServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableGRpcServer
public class InventarioServer {
  public static void main(String[] args) {
    SpringApplication.run(InventarioServer.class, args);
  }

  @PreDestroy
  public void Destroy(){
    System.out.println("mamo...");
  }
}
