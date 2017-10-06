package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProductCatalogService {
  private final Sender sender;
  private final ProductRepository productRepository;
  private final ValidacionService validacionService;

  public ProductCatalogService(Sender sender, ProductRepository productRepository, ValidacionService validacionService) {
    this.sender = sender;
    this.productRepository = productRepository;
    this.validacionService = validacionService;
  }

  public Optional<Product> add(Product newProduct) {
    boolean aceptado = validacionService.createValidacion(newProduct.getName());

    log.info("Se aceoto el producto? {}", aceptado);


    if (aceptado) {
      return Optional.of(productRepository.save(newProduct));

      //TODO: el siguiente bloque de codigo, debe ir en un interceptor de JPA
      /*

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
      */
    } else {
      return Optional.empty();
    }
  }

  public void delete(Long productId) {
    productRepository.delete(productId);

    //TODO: el siguiente bloque de codigo, debe ir en un interceptor de JPA
      /*
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
    */
  }

  public Product update(Long productId, Product newData) {
    // se busca y actualiza
    Product product = productRepository.findOne(productId);
    product.setName(newData.getName());
    product.setDescription(newData.getDescription());
    product.setPrice(newData.getPrice());
    return productRepository.save(product);

    //TODO: el siguiente bloque de codigo, debe ir en un interceptor de JPA
    /*
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
    */
  }
}
