package com.fireflink.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ResponseStructure> handleNoSuchElementExcptn(NoSuchElementException elementException) {
		return ResponseEntity.ok(new ResponseStructure().setStatus(HttpStatus.BAD_REQUEST.value())
				.setHttpStatus(HttpStatus.BAD_REQUEST)
				.setData(elementException.getMessage()));
	}
	
	  @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ResponseStructure> handleIllegalArgumentExcptn(IllegalArgumentException exception) {
	        return ResponseEntity.ok(new ResponseStructure().setStatus(HttpStatus.BAD_REQUEST.value())
	            .setHttpStatus(HttpStatus.BAD_REQUEST)
	            .setData(exception.getMessage()));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ResponseStructure> handleGenericExcptn(Exception exception) {
	        return ResponseEntity.ok(new ResponseStructure().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	            .setData(exception.getMessage()));
	    }

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> message=new HashMap<>();
		ex.getAllErrors().forEach(error -> {
			message.put(((FieldError)error).getField(), error.getDefaultMessage());
		});

		return ResponseEntity.ok().body(new ResponseStructure().setStatus(HttpStatus.BAD_REQUEST.value())
				.setHttpStatus(HttpStatus.BAD_REQUEST)
				.setData(message));

	}
}
