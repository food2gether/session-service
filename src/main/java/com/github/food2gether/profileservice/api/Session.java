package com.github.food2gether.profileservice.api;


import java.util.Date;

public class Session {
    private Integer id;
    private Integer restaurantId;
    private Integer organizerId;
    private Date deadline;

    // Constructors
    public Session() {
    }

    public Session(Integer id, Integer restaurantId, Integer organizerId, Date deadline) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.organizerId = organizerId;
        this.deadline = deadline;
    }

//    public Session(Integer restaurantId, Integer organizerId, Date deadline) {
//        this.restaurantId = restaurantId;
//        this.organizerId = organizerId;
//        this.deadline = deadline;
//    }

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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}

