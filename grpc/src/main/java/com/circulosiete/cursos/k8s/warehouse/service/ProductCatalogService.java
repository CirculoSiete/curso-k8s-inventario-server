package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ProductCatalogService {
  /**
   * @param newProduct
   * @return
   */
  Optional<Product> add(Product newProduct);

  Optional<Product> delete(Long productId);

  Optional<Product> update(Long productId, Product newData);

  void list(Pageable page, Consumer<Stream<Product>> action);
}
