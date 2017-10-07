package com.circulosiete.cursos.k8s.warehouse.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class Product {

  @Tolerate
  public Product() {
    log.debug("Creating a product!");
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private String description;

  //TODO: mejorar mapping para mayor precision
  private BigDecimal price;

  @Column(name = "created_date", updatable = false)
  @CreatedDate
  private Date createdDate;

  @Column(name = "modified_date")
  @LastModifiedDate
  private Date modifiedDate;

}
