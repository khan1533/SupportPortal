package com.fireflink.dto;

import lombok.Data;

@Data
public class UserRequestDto{


	private int id;
	private String username;
	private String useremail;
	private long phoneNo;
	private String role;
	private String privilege;



}
