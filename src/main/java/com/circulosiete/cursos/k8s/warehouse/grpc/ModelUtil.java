package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.ProductRequest;
import com.circulosiete.cursos.k8s.ProductResponse;
import com.circulosiete.cursos.k8s.warehouse.model.Product;

import java.math.BigDecimal;

public class ModelUtil {
  public static Product from(ProductRequest request) {
    return Product.builder()
      .description(request.getDescripcion())
      .name(request.getNombre())
      .price(BigDecimal.valueOf(request.getPrecio()))
      .build();
  }

  public static Product from(ProductResponse request) {
    return Product.builder()
      .description(request.getDescripcion())
      .name(request.getNombre())
      .price(BigDecimal.valueOf(request.getPrecio()))
      .build();
  }

  public static ProductResponse from(Product product) {
    return ProductResponse.newBuilder()
      .setNombre(product.getName())
      .setId(product.getId())
      .setDescripcion(product.getDescription())
      .setPrecio(product.getPrice().intValue())
      .build();
  }
}
