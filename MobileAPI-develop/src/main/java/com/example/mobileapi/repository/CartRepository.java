package com.example.mobileapi.repository;

import com.example.mobileapi.model.Cart;
import com.example.mobileapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByCustomerId(int customerId);

    Optional<Cart> findByCustomer(Customer customer);

    Optional<Cart> findCartByCustomer_Username(String customerUsername);
}
