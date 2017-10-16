package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.CreateRequest;
import com.circulosiete.cursos.k8s.ValidacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidacionService {

  private  String hostname;
  private int port;
  private ManagedChannel channel;
  private ValidacionServiceGrpc.ValidacionServiceBlockingStub stub;
  @Autowired
  public ValidacionService(@Value("${validation-svc.hostname}") String hostname,
                           @Value("${validation-svc.port}") int port){
    this.hostname = hostname;
    this.port = port;
    channel = ManagedChannelBuilder.forAddress(hostname, port)
            .usePlaintext(true)
            .build();
    stub = ValidacionServiceGrpc.newBlockingStub(channel);

  }

  public boolean createValidacion(String nombre) {

    log.info("Validando: {}", nombre);

    return  stub.validacionCreate(CreateRequest.newBuilder()
            .setNombre(nombre)
            .build())
            .getValido();
  }

}
