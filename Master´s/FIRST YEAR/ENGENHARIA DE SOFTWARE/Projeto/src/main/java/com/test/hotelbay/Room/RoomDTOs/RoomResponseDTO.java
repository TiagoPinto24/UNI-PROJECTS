package com.test.hotelbay.Room.RoomDTOs;

import java.math.BigDecimal;
import java.util.List;

import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Room.RoomStatus;

public class RoomResponseDTO {
    private String number;
    private String description;
    private int capacity;
    private BigDecimal price;
    private RoomStatus status;
    private List<Category> categories;
    private Long hotelId;

        public RoomResponseDTO() {
    }

    public RoomResponseDTO(String number, String description, int capacity, BigDecimal price, RoomStatus status, List<Category> categories, Long hotelId) {
        this.number = number;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.categories = categories;
        this.hotelId = hotelId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId= hotelId;
    }

}
