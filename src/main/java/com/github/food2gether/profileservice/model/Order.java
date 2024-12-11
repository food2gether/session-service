package com.github.food2gether.profileservice.api;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Order {

    @Id
    private Integer id;
    private Integer profileId;
    private List<String> entries;

    public Order(Integer id, Integer profileId, List<String> entries) {
        this.id = id;
        this.profileId = profileId;
        this.entries = entries;
    }

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

    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", profileId=" + profileId +
                ", entries=" + entries +
                '}';
    }

//    public static class Entry {
//        private String id;
//        private String name;
//        private String description;
//        private int price;
//        private List<String> allergies;
//
//        public Entry(String id, String name, String description, int price, List<String> allergies) {
//            this.id = id;
//            this.name = name;
//            this.description = description;
//            this.price = price;
//            this.allergies = allergies;
//        }
//
//        // Getters and Setters...
//
//        @Override
//        public String toString() {
//            return "Entry{" +
//                    "id='" + id + '\'' +
//                    ", name='" + name + '\'' +
//                    ", description='" + description + '\'' +
//                    ", price=" + price +
//                    ", allergies=" + allergies +
//                    '}';
//        }
//    }
}