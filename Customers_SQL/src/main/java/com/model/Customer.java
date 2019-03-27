package com.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

@XmlRootElement
@Document
public class Customer {
	
	public Customer(){
		
	}
//	@Id
//	private int id;

	private String name;
	private String mobileNumber;
	private String email;
	private String personalNumber;
	private String password;
//	private String jobFlag;

	private CustomerPreferences preference;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public CustomerPreferences getPreference() {
		return preference;
	}

	public void setPreference(CustomerPreferences preference) {
		this.preference = preference;
	}
	
	@Override
	public String toString() {
		return "Customer [name=" + name + ", mobileNumber=" + mobileNumber + ", email=" + email
				+ ", personalNumber=" + personalNumber + ", passWord=" + password + ", preference=" + preference + "]";
	}
	
	public Customer(String name, String mobileNumber, String email, String personalNumber, String passWord,
			CustomerPreferences preference) {
		super();
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.personalNumber = personalNumber;
		this.password = passWord;
		this.preference = preference;
	}

//	public String getJobFlag() {
//		return jobFlag;
//	}
//
//	public void setJobFlag(String jobFlag) {
//		this.jobFlag = jobFlag;
//	}

}

