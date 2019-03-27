package com.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String>{
	
	public Customer getCustByName(String name);
	public Customer getCustByEmail(String email);
	public List<Customer> findAll();
	
	
}
