package com.fireflink.model;

import org.springframework.data.annotation.Id;

import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
public class User extends BaseEntity{
	
	@Id
	private int id;
	private String username;
	private String useremail;
	private long phoneNo;
	private String role;
	private String privilege;
	private String access;
	private String password;
	private String token;
	private String status;

	@Transient
	private int noOfTickets;
	


}
