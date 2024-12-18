package com.github.food2gether.sessionservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders;

  @Column(name = "restaurant_id", nullable = false)
  private Integer restaurantId;

  @Column(name = "organizer_id", nullable = false)
  private Integer organizerId;

  @Column(name = "deadline", nullable = false)
  private LocalDateTime deadline;

  // Constructors
  public Session() {}

  public Session(Integer restaurantId, Integer organizerId, LocalDateTime deadline) {
    this.restaurantId = restaurantId;
    this.organizerId = organizerId;
    this.deadline = deadline;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(Integer restaurantId) {
    this.restaurantId = restaurantId;
  }

  public Integer getOrganizerId() {
    return organizerId;
  }

  public void setOrganizerId(Integer organizerId) {
    this.organizerId = organizerId;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDateTime deadline) {
    this.deadline = deadline;
  }

  @Override
  public String toString() {
    return "Session{" +
        "id=" + id +
        ", restaurantId=" + restaurantId +
        ", organizerId=" + organizerId +
        ", deadline=" + deadline +
        '}';
  }
}

