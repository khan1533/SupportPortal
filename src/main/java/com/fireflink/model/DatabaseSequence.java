package com.fireflink.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class DatabaseSequence {

	@Id
	private String id;
	private String seq;
}
