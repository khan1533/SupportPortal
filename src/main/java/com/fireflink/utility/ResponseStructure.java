package com.fireflink.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseStructure {

	private int status;
	private HttpStatus httpStatus;
	private Object data;
	public int getStatus() {
		return status;
	}
	public ResponseStructure setStatus(int status) {
		this.status = status;
		return this;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public ResponseStructure setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
		return this;
	}
	public Object getData() {
		return data;
	}
	public ResponseStructure setData(Object data) {
		this.data = data;
		return this;
	}
}
