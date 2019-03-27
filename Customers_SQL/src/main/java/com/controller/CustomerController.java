package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.Customer;
import com.model.CustomerPreferences;
import com.service.CustomerService;

@Controller
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	CustomerService service;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String loadForm(Model model) {
		System.out.println("Enter loadForm()");
		List<Customer> customers = service.getAll();
		if (null != customers && !customers.isEmpty()) {
			System.out.println("Size: "+customers.size());
			model.addAttribute("customerDb", customers);
		} else {
			model.addAttribute("msg", "There are no customers registered !!");
		}
		System.out.println("Exit loadForm()");
		return "customerForm";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String routeToForm() {
		return "customerForm";
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST, params = "create")
	public String createCustomer(@ModelAttribute("customer") Customer customer, Model model) {
		try {
			model.addAttribute("customerDb", service.create(customer));
			return "customerForm";

		} catch (Exception e) {
			e.getStackTrace();
			return "customerForm";
		}
	}

	@RequestMapping("/get")
	public String getByName(@RequestParam String name) {
		Customer customer = service.getByName(name);
		return customer.toString();
	}

	@RequestMapping("/getall")
	public List<Customer> getAll() {
		return service.getAll();
	}

	@RequestMapping("/updatepref")
	public String updatePreference(@RequestParam String name, @RequestParam String rent, @RequestParam String rooms,
			@RequestParam String area) {
		return service.updateByName(name,
				new CustomerPreferences(Integer.parseInt(rent), Integer.parseInt(rooms), Integer.parseInt(area)))
				.toString();
	}

	@RequestMapping("/deletebyname")
	public List<Customer> deleteByName(@RequestParam String name) {
		service.deleteByname(name);
		return service.getAll();
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST, params = "deleteAll")
	public String deleteAll(Model model) {
		service.deleteAll();
		model.addAttribute("customerDb", service.getAll());
		return "customerForm";
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST, params = "runJob")
	public String startJob(@RequestParam String selectedName, Model model) {
		service.startJob(selectedName);
		model.addAttribute("customerDb", service.getAll());
		return "customerForm";
	}

}
