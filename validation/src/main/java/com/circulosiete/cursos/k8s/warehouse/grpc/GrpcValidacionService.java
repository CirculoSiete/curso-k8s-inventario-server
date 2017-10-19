package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.CreateRequest;
import com.circulosiete.cursos.k8s.ValidacionResponse;
import com.circulosiete.cursos.k8s.ValidacionServiceGrpc;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import org.lognet.springboot.grpc.GRpcService;

@Log4j
@GRpcService
public class GrpcValidacionService extends ValidacionServiceGrpc.ValidacionServiceImplBase {


  private final ProductRepository productRepository;

  public GrpcValidacionService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public void validacionCreate(CreateRequest request, StreamObserver<ValidacionResponse> responseObserver) {
    log.info("Validando: " + request.getNombre());

    responseObserver.onNext(ValidacionResponse.newBuilder()
      .setValido(!productRepository.existsByName(request.getNombre()))
      .build());

    responseObserver.onCompleted();
  }
}
