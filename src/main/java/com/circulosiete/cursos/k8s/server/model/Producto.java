package com.circulosiete.cursos.k8s.server.model;

import javax.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {

  private static final long serialVersionUID = -3009157732242241606L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "precio")
  private int precio;

  public Producto(String nombre, String descripcion, int precio) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precio = precio;
  }
}
