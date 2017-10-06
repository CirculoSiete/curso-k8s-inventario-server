package com.circulosiete.cursos.k8s.server.repo;

import com.circulosiete.cursos.k8s.server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Product, Long> {
}
