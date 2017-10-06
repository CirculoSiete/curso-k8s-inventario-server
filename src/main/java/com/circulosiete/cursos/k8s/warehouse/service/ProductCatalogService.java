package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.warehouse.model.Product;

import java.util.Optional;

public interface ProductCatalogService {
  Optional<Product> add(Product newProduct);

  Product delete(Long productId);

  Product update(Long productId, Product newData);
}
