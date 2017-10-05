package com.circulosiete.cursos.k8s.server.repo;

import com.circulosiete.cursos.k8s.server.model.Product;
import org.springframework.data.repository.CrudRepository;



public interface ProductoRepository extends CrudRepository<Product, Long> {
  @Override
  void delete(Long aLong);

  @Override
  Product findOne(Long aLong);


}
