package com.test.hotelbay.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.hotelbay.Account.Account;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    Optional<Category> findByNameAndAdmin(String name, Account admin);

    void deleteByNameAndAdmin(String name, Account admin);

    List<Category> findByAdmin(Account account);

    
} 