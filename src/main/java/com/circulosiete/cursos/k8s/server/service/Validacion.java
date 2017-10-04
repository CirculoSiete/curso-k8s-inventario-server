package com.circulosiete.cursos.k8s.server.service;

import com.circulosiete.cursos.k8s.CreateRequest;
import com.circulosiete.cursos.k8s.ValidacionResponse;
import com.circulosiete.cursos.k8s.ValidacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class Validacion {

  private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 7070)
          .usePlaintext(true)
          .build();

  private ValidacionServiceGrpc.ValidacionServiceBlockingStub stub = ValidacionServiceGrpc.newBlockingStub(channel);

  public boolean createValidacion(String nombre){
    System.out.println("validando "+nombre);
    ValidacionResponse response = stub.validacionCreate(CreateRequest.newBuilder()
            .setNombre(nombre)
            .build());

    return response.getValido();
  }



}
