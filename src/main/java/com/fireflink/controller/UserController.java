package com.fireflink.controller;

import java.util.Optional;

import com.fireflink.dto.UserRequestDto;
import com.fireflink.model.User;
import com.fireflink.responsedto.UserResponseDto;
import com.fireflink.service.UserService;
import com.fireflink.utility.ResponseStructure;
import com.fireflink.utility.SimpleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;



	@GetMapping("/userValidaton")
	public String checkUniqueUserEmail(@RequestParam  String userEmail){
		return userService.checkUniqueUserEmail(userEmail);
	}


	
	@PostMapping
	public ResponseEntity<SimpleResponse> addUser(@RequestBody UserRequestDto userRequestDto, @RequestParam String userEmail) {
		return userService.addUser(userRequestDto, userEmail);
	}
	
	
	
	@GetMapping("/{email}")
	public User findByUseremail(@PathVariable(value = "email")  String useremail) {
		return userService.findByUseremail(useremail);
	}

	@PutMapping("/setPassword")
	public ResponseEntity<ResponseStructure> setPassword(@RequestParam String token, @RequestParam String password){
		return userService.setPassword(token,password);
	}
}
