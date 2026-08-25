package com.test.hotelbay.Hotel.HotelDTOs;

import java.util.ArrayList;
import java.util.List;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Room.Room;

public class HotelDTO {

    private String name;
    private String description;
    private String location;
    private String contact;
    private Boolean active;
    private List<String> comodities;
    private List<String> policies;
    private List<Account> adminList = new ArrayList<>();
    private List<Room> roomList;

    public HotelDTO() {
    }

    public HotelDTO(String name, String description, String location, String contact, Boolean active,
                    List<String> policies, List<String> comodities, List<Account> adminList, List<Room> roomList) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.active = active;
        this.comodities = comodities;
        this.policies = policies;
        this.adminList = adminList != null ? adminList : new ArrayList<>();
        this.roomList = roomList;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getComodities() {
        return comodities;
    }

    public void setComodities(List<String> comodities) {
        this.comodities = comodities;
    }

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public List<Account> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Account> adminList) {
        this.adminList = adminList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
