package com.fireflink.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseStructureAPI {

	
	public static ResponseEntity<ResponseStructure> createResponse(Object obj){
		return ResponseEntity.ok().body(new ResponseStructure().setStatus(HttpStatus.CREATED.value())
				.setHttpStatus(HttpStatus.CREATED)
				.setData(obj));
	}
	
	public static ResponseEntity<ResponseStructure> foundResponse(Object obj){
		return ResponseEntity.ok().body(new ResponseStructure().setStatus(HttpStatus.FOUND.value())
				.setHttpStatus(HttpStatus.FOUND)
				.setData(obj));
	}
	

	
	public static ResponseEntity<ResponseStructure> okResponse(Object obj){
		return ResponseEntity.ok().body(new ResponseStructure().setStatus(HttpStatus.OK.value())
				.setHttpStatus(HttpStatus.OK)
				.setData(obj));
	}
	
}
