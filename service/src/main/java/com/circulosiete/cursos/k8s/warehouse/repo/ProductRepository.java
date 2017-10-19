package com.circulosiete.cursos.k8s.warehouse.repo;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findById(Long id);

  @Query("select p from Product p")
  Stream<Product> streamAllPaged(Pageable pageable);

  Page<Product> findAll(Pageable pageable);

  boolean existsByName(String name);
}
