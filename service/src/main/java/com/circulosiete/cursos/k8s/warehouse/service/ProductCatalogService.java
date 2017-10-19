package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.warehouse.config.Trace;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ProductCatalogService {
  /**
   * @param newProduct
   * @return
   */
  @Trace
  Optional<Product> add(Product newProduct);

  @Trace
  Optional<Product> delete(Long productId);

  @Trace
  Optional<Product> update(Long productId, Product newData);

  @Trace
  void list(Pageable page, Consumer<Stream<Product>> action);

  @Trace
  List<Product> list(Pageable page);

  @Trace
  Page<Product> paged(Pageable page);
}
