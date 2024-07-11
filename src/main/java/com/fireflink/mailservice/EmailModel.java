package com.fireflink.mailservice;

import lombok.Data;

@Data
public class EmailModel {

	private String[] to;
	private String subject;
	private String text;
	
}
