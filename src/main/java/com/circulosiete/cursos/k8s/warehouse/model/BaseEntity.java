package com.circulosiete.cursos.k8s.warehouse.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Version
  private Long version;

  @Column(name = "created_date", updatable = false)
  @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
  @CreatedDate
  private ZonedDateTime createdDate;

  @Column(name = "modified_date")
  @LastModifiedDate
  @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
  private ZonedDateTime modifiedDate;
}
