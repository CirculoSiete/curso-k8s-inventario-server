package com.circulosiete.cursos.k8s.warehouse.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@Slf4j
public class Product extends BaseEntity {

  @Tolerate
  public Product() {
    log.debug("Creating a product!");
  }

  private String name;

  private String description;

  //TODO: mejorar mapping para mayor precision
  private BigDecimal price;

}
