package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.*;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import com.circulosiete.cursos.k8s.warehouse.service.ProductCatalogService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;


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
  public void inventarioCreate(InventarioRequest request, StreamObserver<InventarioResponse> responseObserver) {

    Product product = Product.builder()
      .name(request.getNombre())
      .description(request.getDescripcion())
      .price(BigDecimal.valueOf(request.getPrecio()))
      .build();

    // se manda respuesta cliente GRPC
    responseObserver.onNext(InventarioResponse.newBuilder()
      .setAceptado(productCatalogService.add(product).isPresent())
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void inventarioDelete(IdRequest request, StreamObserver<InventarioResponse> responseObserver) {
    productCatalogService.delete(request.getId());

    // se manda respuesta cliente GRPC
    responseObserver.onNext(InventarioResponse.newBuilder()
      .setAceptado(true)
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void inventarioGet(IdRequest request, StreamObserver<GetResponse> responseObserver) {
    // se obtiene en postgres
    Product product = productRepository.findOne(request.getId());

    // se manda respuesta cliente GRPC
    responseObserver.onNext(GetResponse.newBuilder()
      .setId(product.getId())
      .setNombre(product.getName())
      .setDescripcion(product.getDescription())
      .setPrecio(product.getPrice().intValue())
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void inventarioUpdate(GetResponse request, StreamObserver<InventarioResponse> responseObserver) {
    Product product = Product.builder()
      .name(request.getNombre())
      .description(request.getDescripcion())
      .price(BigDecimal.valueOf(request.getPrecio()))
      .build();

    productCatalogService.update(request.getId(), product);

    // se manda respuesta cliente GRPC
    responseObserver.onNext(InventarioResponse.newBuilder()
      .setAceptado(true)
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();

  }
}