package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.*;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import com.circulosiete.cursos.k8s.warehouse.service.ProductCatalogService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;

import static com.circulosiete.cursos.k8s.warehouse.grpc.ModelUtil.*;
import static java.util.stream.Collectors.toList;


@Slf4j
@GRpcService
public class GrpcInventarioService extends ProductServiceGrpc.ProductServiceImplBase {

  private final ProductCatalogService productCatalogService;
  private final ProductRepository productRepository;

  public GrpcInventarioService(ProductCatalogService productCatalogService, ProductRepository productRepository) {
    this.productCatalogService = productCatalogService;
    this.productRepository = productRepository;
  }

  @Override
  public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
    responseObserver.onNext(from(productCatalogService.add(from(request))));
    responseObserver.onCompleted();
  }

  @Override
  public void read(EntityId request, StreamObserver<ProductResponse> responseObserver) {
    responseObserver.onNext(from(productRepository.findById(request.getId())));
    responseObserver.onCompleted();
  }

  @Override
  public void update(ProductModel request, StreamObserver<ProductResponse> responseObserver) {
    responseObserver.onNext(from(productCatalogService.update(request.getId(), from(request))));
    responseObserver.onCompleted();
  }

  @Override
  public void delete(EntityId request, StreamObserver<ProductResponse> responseObserver) {
    responseObserver.onNext(from(productCatalogService.delete(request.getId())));
    responseObserver.onCompleted();
  }

  @Override
  public void list(Page request, StreamObserver<ProductResponse> responseObserver) {
    productCatalogService.list(from(request),
      productsStream -> productsStream.forEach(
        product -> responseObserver.onNext(from(product))));

    responseObserver.onCompleted();
  }

  @Override
  public void paged(Page request, StreamObserver<PageProductResponse> responseObserver) {
    org.springframework.data.domain.Page<Product> pagedResult = productCatalogService
      .paged(from(request));

    List<ProductModel> list = pagedResult
      .getContent().stream()
      .map(ModelUtil::fromProduct)
      .collect(toList());

    PageProductResponse result = PageProductResponse.newBuilder()
      .addAllContent(list)
      .setFirst(boolValue(pagedResult.isFirst()))
      .setLast(boolValue(pagedResult.isLast()))
      .setNumber(intValue(pagedResult.getNumber()))
      .setNumberOfElements(intValue(pagedResult.getNumberOfElements()))
      .setSize(intValue(pagedResult.getSize()))
      .setTotalElements(longValue(pagedResult.getTotalElements()))
      .setTotalPages(intValue(pagedResult.getTotalPages()))
      .build();

    responseObserver.onNext(result);
    responseObserver.onCompleted();
  }

}