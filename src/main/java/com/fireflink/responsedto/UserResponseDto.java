package com.fireflink.responsedto;

import lombok.Data;

@Data
public class UserResponseDto{

	private int id;
	private String username;
	private long phoneNo;
	private String role;
	private String privilege;
	private String access;
	private String status;
	private String createdByName;
	private String createdOn;

}
