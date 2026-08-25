package com.test.hotelbay.Hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.hotelbay.Account.Account;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>{

    Optional<Hotel> findByName(String name);

    void deleteByName(String name);

    List<Hotel> findByAdminListContains(Account account);

}
