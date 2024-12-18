package com.github.food2gether.sessionservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class Entry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "allergies")
  private String allergies;

  // Constructors
  public Entry() {}

  public Entry(String name, String description, Integer price, String allergies) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.allergies = allergies;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getAllergies() {
    return allergies;
  }

  public void setAllergies(String allergies) {
    this.allergies = allergies;
  }

  @Override
  public String toString() {
    return "Entry{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", allergies=" + allergies +
        '}';
  }
}