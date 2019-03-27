package com.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

@XmlRootElement
@Document
public class CustomerPreferences {
	
	public CustomerPreferences(int rent, int rooms, int area) {
		super();
		this.rent = rent;
		this.rooms = rooms;
		this.area = area;
	}
	public CustomerPreferences() {
		// TODO Auto-generated constructor stub
	}
	enum addType{
		FIRST_HAND, SECOND_HAND, NEW_BUILT
	}
	
	enum city{
		GOTHENBURG;
	}
	
	private int rent;
	private int rooms;
	private int area;
	public int getRent() {
		return rent;
	}
	public void setRent(int rent) {
		this.rent = rent;
	}
	public int getRooms() {
		return rooms;
	}
	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	
}
