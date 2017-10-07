package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.EntityId;
import com.circulosiete.cursos.k8s.InventarioServiceGrpc;
import com.circulosiete.cursos.k8s.ProductRequest;
import com.circulosiete.cursos.k8s.ProductResponse;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import com.circulosiete.cursos.k8s.warehouse.service.ProductCatalogService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import static com.circulosiete.cursos.k8s.warehouse.grpc.ModelUtil.from;


@Slf4j
@GRpcService
public class GrpcInventarioService extends InventarioServiceGrpc.InventarioServiceImplBase {

  private final ProductCatalogService productCatalogService;
  private final ProductRepository productRepository;

  public GrpcInventarioService(ProductCatalogService productCatalogService, ProductRepository productRepository) {
    this.productCatalogService = productCatalogService;
    this.productRepository = productRepository;
  }


  @Override
  public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {

    productCatalogService.add(from(request))
      .ifPresent(newProduct -> responseObserver.onNext(from(newProduct)));

    responseObserver.onCompleted();
  }

  @Override
  public void read(EntityId request, StreamObserver<ProductResponse> responseObserver) {

    Product product = productRepository.findById(request.getId())
      .orElse(null);

    responseObserver.onNext(from(product));

    responseObserver.onCompleted();
  }

  @Override
  public void update(ProductResponse request, StreamObserver<ProductResponse> responseObserver) {
    Product product = from(request);

    productCatalogService.update(request.getId(), product)
      .ifPresent(product1 -> responseObserver.onNext(from(product1)));

    responseObserver.onCompleted();
  }

  @Override
  public void delete(EntityId request, StreamObserver<ProductResponse> responseObserver) {

    productCatalogService.delete(request.getId())
      .ifPresent(product -> responseObserver.onNext(from(product)));

    responseObserver.onCompleted();
  }

}