package com.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
