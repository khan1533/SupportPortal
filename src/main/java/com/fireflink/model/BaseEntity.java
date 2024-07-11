package com.fireflink.model;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class BaseEntity {

	
	private String createdByName;
	private String modifiedByName;
	private String createByEmail;
	private String modifiedByByEmail;
	private String createdOn;
	
	private String modifiedOn;
	
	public void createBaseEntity(String name, String email) {
		createdByName=name;
		createByEmail=email;
		createdOn=LocalDateTime.now().toString();
		
	}
	
	
	public void modifiedBaseEntity(String name, String email) {
		modifiedByName=name;
		modifiedByByEmail=email;
		modifiedOn=LocalDateTime.now().toString();
		
	}
}
