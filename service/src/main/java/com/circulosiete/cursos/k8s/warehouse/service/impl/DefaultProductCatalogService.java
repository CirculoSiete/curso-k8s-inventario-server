package com.circulosiete.cursos.k8s.warehouse.service.impl;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import com.circulosiete.cursos.k8s.warehouse.service.ProductCatalogService;
import com.circulosiete.cursos.k8s.warehouse.service.ValidacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class DefaultProductCatalogService implements ProductCatalogService {
  private final ProductRepository productRepository;
  private final ValidacionService validacionService;

  public DefaultProductCatalogService(ProductRepository productRepository, ValidacionService validacionService) {
    this.productRepository = productRepository;
    this.validacionService = validacionService;
  }

  @Override
  public Optional<Product> add(Product newProduct) {
    boolean aceptado = validacionService.createValidacion(newProduct.getName());

    log.info("Se acepto el producto? {}", aceptado);

    if (aceptado) {
      return Optional.of(productRepository.save(newProduct));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Product> delete(Long productId) {

    return productRepository.findById(productId).map(product -> {
      productRepository.delete(product);
      return product;
    });

  }

  @Override
  public Optional<Product> update(Long productId, Product newData) {

    return productRepository.findById(productId).map(product -> {
      product.setName(newData.getName());
      product.setDescription(newData.getDescription());
      product.setPrice(newData.getPrice());
      return productRepository.save(product);
    });

  }

  @Override
  public void list(Pageable page, Consumer<Stream<Product>> action) {
    try (Stream<Product> productStream = productRepository.streamAllPaged(page)) {
      action.accept(productStream);
    }
  }

  @Override
  public List<Product> list(Pageable page) {
    List<Product> result = new ArrayList<>();

    list(page, productStream ->
      productStream.forEach(result::add));

    return result;
  }

  @Override
  public Page<Product> paged(Pageable page) {
    return productRepository.findAll(page);
  }

}
