package com.github.food2gether.sessionservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "profile_id", nullable = false)
  private Integer profileId;

  @ManyToOne
  @JoinColumn(name = "session_id", nullable = false)
  private Session session;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id")
  private List<Entry> entries;

  // Constructors
  public Order() {}

  public Order(Integer profileId, Session session, List<Entry> entries) {
    this.profileId = profileId;
    this.session = session;
    this.entries = entries;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getProfileId() {
    return profileId;
  }

  public void setProfileId(Integer profileId) {
    this.profileId = profileId;
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", profileId=" + profileId +
        ", session=" + session +
        ", entries=" + entries +
        '}';
  }
}