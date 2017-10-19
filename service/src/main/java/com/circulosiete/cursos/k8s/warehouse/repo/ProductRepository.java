package com.circulosiete.cursos.k8s.warehouse.repo;

import com.circulosiete.cursos.k8s.warehouse.config.Trace;
import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Trace
  Optional<Product> findById(Long id);

  @Trace
  @Query("select p from Product p")
  Stream<Product> streamAllPaged(Pageable pageable);

  @Trace
  Page<Product> findAll(Pageable pageable);

  @Trace
  boolean existsByName(String name);
}
