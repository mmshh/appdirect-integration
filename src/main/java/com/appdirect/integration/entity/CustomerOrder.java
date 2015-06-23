package com.appdirect.integration.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class CustomerOrder {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	private String customerName;
	
	private String editionCode;
	
	public CustomerOrder(String customerName, String editionCode){
		
		this.customerName = customerName;
		this.editionCode = editionCode;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEditionCode() {
		return editionCode;
	}

	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}
}
