package com.circulosiete.cursos.k8s.warehouse.repo;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Product, Long> {
}
