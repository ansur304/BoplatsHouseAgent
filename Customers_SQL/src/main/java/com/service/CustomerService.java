package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.job.AutoRegister;
import com.model.Customer;
import com.model.CustomerPreferences;
import com.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository repository;

	@Autowired
	AutoRegister autoJob;
	
	

	// CRUD operations

	// Create
	public List<Customer> create(Customer customer) {
		repository.save(customer);
		return repository.findAll();

	}

	// Read or Retrieve
	public List<Customer> getAll() {
		return repository.findAll();
	}

	// Read by name
	public Customer getByName(String name) {
		return repository.getCustByName(name);
	}

	// Update preferences by using name
	public Customer updateByName(String name, CustomerPreferences preference) {
		Customer customer = repository.getCustByName(name);
		if (null != customer) {
			customer.setPreference(preference);
			return repository.save(customer);
		} else {
			return null;
		}
	}

	// delete by name
	public void deleteByname(String name) {
		Customer customer = new Customer();
		customer.setName(name);
		repository.delete(customer);
	}

	// delete all
	public void deleteAll() {
		repository.deleteAll();
	}

	public boolean startJob(String name) {
		Boolean result = false;
		Customer customer = getByName(name);
		if(null != customer){
			autoJob.startJob(customer);
		}
		
		return result;
	}
}
