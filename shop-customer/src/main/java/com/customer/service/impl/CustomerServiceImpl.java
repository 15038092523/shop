package com.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.customer.entity.Customer;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    public Customer create(Customer customer) {
        customer = customerRepository.save(customer);
        String customerStr = JSON.toJSONString(customer);
        stringRedisTemplate.opsForValue().set(customer.getId(), customerStr);
        return customer;
    }

    @Override
    public Customer findById(String id) {
        String customerStr = stringRedisTemplate.opsForValue().get(id);
        Customer customer = null;
        if (!StringUtils.isBlank(customerStr)) {
            customer = JSON.parseObject(customerStr, Customer.class);
            return customer;
        }
        if (customer == null) {
            customer = customerRepository.findOne(id);
            if (customer != null) {
                customerStr = JSON.toJSONString(customer);
                stringRedisTemplate.opsForValue().set(customer.getId(), customerStr);
            }
        }
        return customer;

    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
