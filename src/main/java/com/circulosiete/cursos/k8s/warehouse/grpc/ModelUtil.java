package com.circulosiete.cursos.k8s.warehouse.grpc;

import com.circulosiete.cursos.k8s.*;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

public class ModelUtil {
  public static Product from(ProductRequest request) {
    return Product.builder()
      .description(request.getDescription())
      .name(request.getName())
      .price(new BigDecimal(request.getPrice()))
      .build();
  }

  public static Product from(ProductModel request) {
    return Product.builder()
      .description(request.getDescription())
      .name(request.getName())
      .price(new BigDecimal(request.getPrice()))
      .build();
  }

  public static Product from(ProductResponse request) {
    return from(request.getProduct());
  }

  public static ProductResponse from(Product found) {
    return from(Optional.ofNullable(found));
  }

  public static ProductResponse from(Optional<Product> found) {

    ProductResponse.Builder builderResponse = ProductResponse.newBuilder();

    found.ifPresent(product -> {
      ProductModel productModel = ProductResponse.newBuilder().getProductBuilder()
        .setName(product.getName())
        .setId(product.getId())
        .setDescription(product.getDescription())
        .setPrice(product.getPrice().toString())
        .setCreatedAt(from(product.getCreatedDate()))
        .setModifiedDate(from(product.getModifiedDate()))
        .setVersion(product.getVersion())
        .build();

      builderResponse.setProduct(productModel);
    });

    return builderResponse.build();
  }

  public static Timestamp from(ZonedDateTime date) {
    Date from = Date.from(date.toInstant());
    long millis = from.getTime();
    return Timestamp.newBuilder()
      .setSeconds(millis / 1000)
      .setNanos((int) ((millis % 1000) * 1000000))
      .build();
  }

  public static Pageable createPageRequest(Integer page, Integer size) {
    int thePage = 0;
    if (page != null) {
      thePage = page;
    }

    int theSize = 10;
    if (size != null && size > 0 && size <= 100) {
      theSize = size;
    }

    return new PageRequest(thePage, theSize);
  }

  public static Pageable from(Page page) {
    return createPageRequest(page.getPage(), page.getSize());
  }
}
