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

import java.util.Optional;

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
    Product product = from(request);

    Optional<Product> potencialNewProduct = productCatalogService.add(product);

    potencialNewProduct.ifPresent(newProduct -> {
      responseObserver.onNext(from(newProduct));
    });

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void read(EntityId request, StreamObserver<ProductResponse> responseObserver) {
    Product product = productRepository.findOne(request.getId());

    // se manda respuesta cliente GRPC
    responseObserver.onNext(from(product));

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void update(ProductResponse request, StreamObserver<ProductResponse> responseObserver) {
    Product product = from(request);

    Product result = productCatalogService.update(request.getId(), product);

    // se manda respuesta cliente GRPC
    responseObserver.onNext(from(result));

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void delete(EntityId request, StreamObserver<ProductResponse> responseObserver) {

    Product deleted = productCatalogService.delete(request.getId());

    // se manda respuesta cliente GRPC
    responseObserver.onNext(from(deleted));

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }


}