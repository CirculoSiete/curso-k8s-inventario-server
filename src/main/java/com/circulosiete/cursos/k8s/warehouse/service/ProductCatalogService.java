package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.warehouse.model.Product;

import java.util.Optional;

public interface ProductCatalogService {
  /**
   *
   * @param newProduct
   * @return
   */
  Optional<Product> add(Product newProduct);

  Optional<Product> delete(Long productId);

  Optional<Product> update(Long productId, Product newData);
}
