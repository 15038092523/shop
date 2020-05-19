package com.customer.service;

import com.customer.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer create(Customer student);

    Customer findById(String id);

    List<Customer> findAll();
}
