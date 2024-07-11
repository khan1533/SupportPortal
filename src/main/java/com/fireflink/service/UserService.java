package com.fireflink.service;

import java.util.Optional;

import com.fireflink.dto.UserRequestDto;
import com.fireflink.model.User;
import com.fireflink.utility.ResponseStructure;
import com.fireflink.utility.SimpleResponse;
import org.springframework.http.ResponseEntity;


public interface UserService {



	
	User findByUseremail(String email);

	User findByRoleIgnoreCase(String admin);


	String checkUniqueUserEmail(String userEmail);

	ResponseEntity<SimpleResponse> addUser(UserRequestDto userRequestDto, String userEmail);

    ResponseEntity<ResponseStructure> setPassword(String token, String password);
}
