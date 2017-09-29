package com.circulosiete.cursos.k8s.server.repo;

import com.circulosiete.cursos.k8s.server.model.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository<Producto, Long> {
}
