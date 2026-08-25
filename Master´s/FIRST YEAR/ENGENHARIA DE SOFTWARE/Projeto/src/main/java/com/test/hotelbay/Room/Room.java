package com.test.hotelbay.Room;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Hotel.Hotel;

@Entity
public class Room {
    
    public Room() {}

    public Room(String number, String description, int capacity, BigDecimal price, RoomStatus status, List<Category> categories, Hotel hotel) {
        this.number = number;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.categories = categories;
        this.hotel = hotel;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String number;
    private String description;
    private int capacity;
    private BigDecimal price;
    private RoomStatus status;
    @ManyToMany
    @JoinTable(
        name = "room_categories",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
    @ManyToOne
    private Hotel hotel;

    public Long getId() {
        return this.id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public RoomStatus getStatus() {
        return this.status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setAvailable(boolean available) {
        this.status = available
                ? RoomStatus.AVAILABLE
                : RoomStatus.OCCUPIED;
    }

}