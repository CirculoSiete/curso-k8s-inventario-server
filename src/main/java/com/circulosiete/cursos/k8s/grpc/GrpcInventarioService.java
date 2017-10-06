package com.circulosiete.cursos.k8s.grpc;

import com.circulosiete.cursos.k8s.*;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductoRepository;
import com.circulosiete.cursos.k8s.warehouse.service.Sender;
import com.circulosiete.cursos.k8s.warehouse.service.ValidacionService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.grpc.spring.boot.autoconfigure.annotation.GRpcService;

import java.math.BigDecimal;


@Slf4j
@GRpcService
public class GrpcInventarioService extends InventarioServiceGrpc.InventarioServiceImplBase {

  private final Sender sender;
  private ProductoRepository productoRepository;
  private ValidacionService validacionService;
  private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

  public GrpcInventarioService(Sender sender, ProductoRepository productoRepository, ValidacionService validacionService) {
    this.sender = sender;
    this.productoRepository = productoRepository;
    this.validacionService = validacionService;
  }

  @Override
  public void inventarioCreate(InventarioRequest request, StreamObserver<InventarioResponse> responseObserver) {

    boolean aceptado = validacionService.createValidacion(request.getNombre());

    log.info("Se aceoto el producto? {}", aceptado);

    if (aceptado) {

      Product product = Product.builder()
        .name(request.getNombre())
        .description(request.getDescripcion())
        .price(BigDecimal.valueOf(request.getPrecio()))
        .build();

      productoRepository.save(product);

      // se crea json para encolar
      JsonObject queueObject = new JsonObject();
      queueObject.addProperty("nombre", request.getNombre());
      queueObject.addProperty("descripcion", request.getDescripcion());
      queueObject.addProperty("precio", request.getPrecio());

      JsonObject queueJson = new JsonObject();
      queueJson.addProperty("tipo", "producto");
      queueJson.addProperty("operacion", "alta");
      queueJson.add("data", queueObject);

      // mandar a cola de rabbit
      sender.sendToRabbitmq(gson.toJson(queueJson));
    }

    // se manda respuesta cliente GRPC
    responseObserver.onNext(InventarioResponse.newBuilder()
      .setAceptado(aceptado)
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void inventarioDelete(IdRequest request, StreamObserver<InventarioResponse> responseObserver) {
    Product product = productoRepository.findOne(request.getId());
    productoRepository.delete(request.getId());

    // se crea json para encolar
    JsonObject queueObject = new JsonObject();
    queueObject.addProperty("id", request.getId());
    queueObject.addProperty("nombre", product.getName());
    queueObject.addProperty("descripcion", product.getDescription());
    queueObject.addProperty("precio", product.getPrice());

    JsonObject queueJson = new JsonObject();
    queueJson.addProperty("tipo", "product");
    queueJson.addProperty("operacion", "borrado");
    queueJson.add("data", queueObject);

    // mandar a cola de rabbit
    sender.sendToRabbitmq(gson.toJson(queueJson));

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
    Product product = productoRepository.findOne(request.getId());

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
    // se busca y actualiza
    Product product = productoRepository.findOne(request.getId());
    product.setName(request.getNombre());
    product.setDescription(request.getDescripcion());
    product.setPrice(BigDecimal.valueOf(request.getPrecio()));
    productoRepository.save(product);

    // se crea json para encolar
    JsonObject queueObject = new JsonObject();
    queueObject.addProperty("id", product.getId());
    queueObject.addProperty("nombre", product.getName());
    queueObject.addProperty("descripcion", product.getDescription());
    queueObject.addProperty("precio", product.getPrice());

    JsonObject queueJson = new JsonObject();
    queueJson.addProperty("tipo", "product");
    queueJson.addProperty("operacion", "actualizacion");
    queueJson.add("data", queueObject);

    // mandar a cola de rabbit
    System.out.println(gson.toJson(queueJson));
    sender.sendToRabbitmq(gson.toJson(queueJson));

    // se manda respuesta cliente GRPC
    responseObserver.onNext(InventarioResponse.newBuilder()
      .setAceptado(true)
      .build());

    // se cierra canal GRPC
    responseObserver.onCompleted();

  }
}