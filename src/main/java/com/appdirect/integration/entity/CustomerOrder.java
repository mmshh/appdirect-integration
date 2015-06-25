package com.appdirect.integration.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CustomerOrder {
	 
	@Id
    String id;
	
	String customerName;
	
	String editionCode;
	
	public CustomerOrder(){
		
	}
	
	public CustomerOrder(String id, String customerName, String editionCode){
		
		this.id = id;
		this.customerName = customerName;
		this.editionCode = editionCode;
	}
	
	@Override
	public String toString(){
		return "Customer Name: "+ customerName + "\nEdition Code: "+ editionCode + "\nUUID: "+ id;
	}
}
