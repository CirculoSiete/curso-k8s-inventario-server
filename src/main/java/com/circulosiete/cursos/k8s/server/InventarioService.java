package com.circulosiete.cursos.k8s.server;

import com.circulosiete.cursos.k8s.InventarioRequest;
import com.circulosiete.cursos.k8s.InventarioResponse;
import com.circulosiete.cursos.k8s.InventarioServiceGrpc;

import com.circulosiete.cursos.k8s.server.model.Producto;
import com.circulosiete.cursos.k8s.server.repo.ProductoRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.grpc.spring.boot.autoconfigure.annotation.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.circulosiete.cursos.k8s.server.service.Sender;


@Slf4j
@GRpcService
public class InventarioService extends InventarioServiceGrpc.InventarioServiceImplBase{

  private Logger logger = LoggerFactory.getLogger(InventarioService.class);
  private final Sender sender;
  private ProductoRepository productoRepository;

  public InventarioService(Sender sender, ProductoRepository productoRepository){
    this.sender = sender;
    this.productoRepository = productoRepository;
  }

  @Override
  public void inventarioHandler(InventarioRequest request, StreamObserver<InventarioResponse> responseObserver){

    // analizar request

    productoRepository.save(new Producto("nombre", "descripcion", 80));
    System.out.println(request);

    sender.sendToRabbitmq("mensaje");
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