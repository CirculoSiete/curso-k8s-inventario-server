package com.circulosiete.cursos.k8s.warehouse.web;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import com.circulosiete.cursos.k8s.warehouse.repo.ProductRepository;
import com.circulosiete.cursos.k8s.warehouse.service.ProductCatalogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.circulosiete.cursos.k8s.warehouse.model.ModelInterceptor.entry;
import static com.circulosiete.cursos.k8s.warehouse.model.ModelInterceptor.from;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
  private final ProductCatalogService productCatalogService;
  private final ProductRepository productRepository;

  public ProductController(ProductCatalogService productCatalogService, ProductRepository productRepository) {
    this.productCatalogService = productCatalogService;
    this.productRepository = productRepository;
  }

  @GetMapping("/{id}")
  public Map getProduc(@PathVariable("id") Long id) {
    return build(productRepository.findById(id));
  }

  @PostMapping
  public Map newProduct(Product product) {
    return build(productCatalogService.add(product));
  }

  @DeleteMapping("/{id}")
  public Map delete(@PathVariable("id") Long id) {
    return build(productCatalogService.delete(id));
  }

  @PutMapping("/{id}")
  public Map edit(@PathVariable("id") Long id, Product product) {
    return build(productCatalogService.update(id, product));
  }

  @GetMapping("/stream")
  public List<Product> list(Pageable pageable) {

    List<Product> result = new ArrayList<>();
    productCatalogService.list(pageable, productStream ->
      productStream.forEach(result::add));

    return result;
  }

  @GetMapping
  public Page<Product> paged(Pageable pageable) {
    return productCatalogService.paged(pageable);
  }

  Map build(Optional<Product> potencialProduct) {
    return potencialProduct
      .map(product -> from(entry("product", product)))
      .orElseGet(Collections::emptyMap);
  }
}
