package com.circulosiete.cursos.k8s.server;

import com.circulosiete.cursos.k8s.*;
import com.circulosiete.cursos.k8s.server.model.Producto;
import com.circulosiete.cursos.k8s.server.repo.ProductoRepository;
import com.circulosiete.cursos.k8s.server.service.Sender;
import com.circulosiete.cursos.k8s.server.service.Validacion;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.grpc.spring.boot.autoconfigure.annotation.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


@Slf4j
@GRpcService
public class InventarioService extends InventarioServiceGrpc.InventarioServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(InventarioService.class);
  private final Sender sender;
  private ProductoRepository productoRepository;
  private Validacion validacion;
  private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

  public InventarioService(Sender sender, ProductoRepository productoRepository, Validacion validacion) {
    this.sender = sender;
    this.productoRepository = productoRepository;
    this.validacion = validacion;
  }

  @Override
  public void inventarioCreate(InventarioRequest request, StreamObserver<InventarioResponse> responseObserver) {
    boolean aceptado = false;
    // se guarda en postgres
    if (validacion.createValidacion(request.getNombre())) {
      log.info("es valido");

      Producto product = Producto.builder()
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

      aceptado = true;
    } else {
      System.out.println("no es valido");
      aceptado = false;
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
    // se borra en postgres
    Producto producto = productoRepository.findOne(request.getId());
    productoRepository.delete(request.getId());
    // productoRepository.delete();

    // se crea json para encolar
    JsonObject queueObject = new JsonObject();
    queueObject.addProperty("id", request.getId());
    queueObject.addProperty("nombre", producto.getName());
    queueObject.addProperty("descripcion", producto.getDescription());
    queueObject.addProperty("precio", producto.getPrice());

    JsonObject queueJson = new JsonObject();
    queueJson.addProperty("tipo", "producto");
    queueJson.addProperty("operacion", "borrado");
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

  @Override
  public void inventarioGet(IdRequest request, StreamObserver<GetResponse> responseObserver) {
    // se obtiene en postgres
    Producto producto = productoRepository.findOne(request.getId());

    // se manda respuesta cliente GRPC
    responseObserver.onNext(GetResponse.newBuilder()
      .setId(producto.getId())
      .setNombre(producto.getName())
      .setDescripcion(producto.getDescription())
      .setPrecio(producto.getPrice().intValue())
      .build());
    // se cierra canal GRPC
    responseObserver.onCompleted();
  }

  @Override
  public void inventarioUpdate(GetResponse request, StreamObserver<InventarioResponse> responseObserver) {
    // se busca y actualiza
    Producto producto = productoRepository.findOne(request.getId());
    producto.setName(request.getNombre());
    producto.setDescription(request.getDescripcion());
    producto.setPrice(BigDecimal.valueOf(request.getPrecio()));
    productoRepository.save(producto);

    // se crea json para encolar
    JsonObject queueObject = new JsonObject();
    queueObject.addProperty("id", producto.getId());
    queueObject.addProperty("nombre", producto.getName());
    queueObject.addProperty("descripcion", producto.getDescription());
    queueObject.addProperty("precio", producto.getPrice());

    JsonObject queueJson = new JsonObject();
    queueJson.addProperty("tipo", "producto");
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