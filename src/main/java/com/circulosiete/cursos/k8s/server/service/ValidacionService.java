package com.circulosiete.cursos.k8s.server.service;

import com.circulosiete.cursos.k8s.CreateRequest;
import com.circulosiete.cursos.k8s.ValidacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidacionService {

  //TODO: remover el codigo duro. Estos valores deben ir en configuracion
  private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 7070)
    .usePlaintext(true)
    .build();

  private ValidacionServiceGrpc.ValidacionServiceBlockingStub stub = ValidacionServiceGrpc.newBlockingStub(channel);

  public boolean createValidacion(String nombre) {

    log.info("Validando: {}", nombre);

    return stub.validacionCreate(CreateRequest.newBuilder()
      .setNombre(nombre)
      .build()).getValido();
  }

}
