package com.test.hotelbay.Hotel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Room.Room;

@Entity
public class Hotel {
    public Hotel() {}

    public Hotel(String name, String description, String location, String contact, List<String> comodities,
            List<String> policies, List<Account> adminList, List<Room> roomList) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.comodities = comodities;
        this.policies = policies;
        this.adminList = adminList;
        this.roomList = roomList;
        this.active = true;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String location;
    private String contact;
    private Boolean active;
    @ElementCollection
    private List<String> comodities;
    @ElementCollection
    private List<String> policies;
    @ManyToMany
    @JoinTable(
        name = "hotel_admin",
        joinColumns = @JoinColumn(name = "hotel_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> adminList = new ArrayList<>();
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> roomList;
        
    public Long getId() {
        return id;
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
        return this.roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
