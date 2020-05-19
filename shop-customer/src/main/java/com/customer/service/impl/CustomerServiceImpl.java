package com.customer.service.impl;

import com.customer.entity.Customer;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        customer = customerRepository.save(customer);
        return customer;
    }

    @Override
    public Customer findById(String id) {
        Customer customer = customerRepository.findOne(id);
        /*//String result = redisService.get(id);
        if (!StringUtils.isBlank(result)) {
          //  customer = JSONObject.parseObject(result, Customer.class);
        }
        if (customer == null) {
            //customer = customerRepository.findOne(id);
        }*/
        return customer;

    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
