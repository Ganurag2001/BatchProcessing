package com.batch.config;

import org.springframework.batch.item.ItemProcessor;

import com.batch.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer customer) throws Exception {
		return customer;
	}

}
