package com.circulosiete.cursos.k8s.warehouse.repo;

import com.circulosiete.cursos.k8s.warehouse.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTests {
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private ProductRepository productRepository;


  @Test
  public void whenFindByName_thenReturnEmployee() {
    // given
    Product tenis = Product.builder()
      .description("Unos tenis")
      .name("Nike Foo")
      .price(BigDecimal.valueOf(1200.0))
      .build();

    Long id = entityManager.persist(tenis).getId();
    entityManager.flush();

    // when
    Product found = productRepository.findOne(id);

    // then
    assertThat(found.getName())
      .isEqualTo(tenis.getName());
  }


}
