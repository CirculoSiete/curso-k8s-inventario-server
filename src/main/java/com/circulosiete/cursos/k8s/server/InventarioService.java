package com.circulosiete.cursos.k8s.server;

import com.circulosiete.cursos.k8s.InventarioRequest;
import com.circulosiete.cursos.k8s.InventarioResponse;
import com.circulosiete.cursos.k8s.InventarioServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.grpc.spring.boot.autoconfigure.annotation.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.circulosiete.cursos.k8s.server.service.Sender;

import java.io.IOException;

@Slf4j
@GRpcService
public class InventarioService extends InventarioServiceGrpc.InventarioServiceImplBase{

  private Logger logger = LoggerFactory.getLogger(InventarioService.class);
  private final Sender sender;

  public InventarioService(Sender sender){
    this.sender = sender;
  }

  @Override
  public void inventarioHandler(InventarioRequest request, StreamObserver<InventarioResponse> responseObserver){

    // analizar request
    System.out.println(request);
    sender.sendToRabbitmq("mensaje lol xd");
    logger.debug("Request " + request);

    // se manda respuesta
    responseObserver.onNext(InventarioResponse.newBuilder()
            .setQueue(true)
            .setDb(true)
            .build());
    // se cierra
    responseObserver.onCompleted();
  }
}