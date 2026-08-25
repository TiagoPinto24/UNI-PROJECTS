package com.test.hotelbay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomStatus;

@Component
public class ScenarioContext {
    private ResultActions resultActions;
    private String url;

    private Account existingAccount = new Account("test@email.com", "John Doe", "JohnDoe123", "Password123", AccountType.GUEST);
    private Account existingAdminAccount = new Account("admin@email.com", "Admin Lopez", "AdminLopez456", "Password123", AccountType.ADMIN);
    private Hotel existingHotel = new Hotel("Grand Hotel", "A 4 star hotel situated in the middle of Lisbon", "Lisbon", "grandhotel@email.com", 
        new ArrayList<>(List.of("spa", "pool", "restaurant")), new ArrayList<>(List.of()), new ArrayList<>(List.of()), new ArrayList<>());
    private  Room existingRoom = new Room("121", "A luxurious room with a nice view to the city", 2, new BigDecimal(599.00), RoomStatus.AVAILABLE, new ArrayList<>(List.of()), existingHotel);
   

    public void setResultActions(ResultActions resultActions) {
        this.resultActions = resultActions;
    }

    public ResultActions getResultActions() {
        return resultActions;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Account getExistingAccount() {
        return existingAccount;
    }

    public void setExistingAccount(Account existingAccount) {
        this.existingAccount = existingAccount;
    }

    public Account getExistingAdminAccount() {
        return existingAdminAccount;
    }

    public void setExistingAdminAccount(Account existingAdminAccount) {
        this.existingAdminAccount = existingAdminAccount;
    }

    public Hotel getExistingHotel() {
        return existingHotel;
    }

    public void setExistingHotel(Hotel existingHotel) {
        this.existingHotel = existingHotel;
    }

    public Room getExistingRoom() {
        return existingRoom;
    }

    public void setExistingRoom(Room existingRoom) {
        this.existingRoom = existingRoom;
    }
}