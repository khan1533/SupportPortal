package com.fireflink.dao.daoimpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.fireflink.model.User;
import org.springframework.stereotype.Component;

import com.fireflink.dao.UserDao;
import com.fireflink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao{
	
	private final UserRepository userRepository;

	@Override
	public User addUser(User user) {
	return	userRepository.save(user);
	}

	

	@Override
	public User findByUseremail(String email) {

	return userRepository.findByUseremail(email).orElseThrow(() ->  new NoSuchElementException("invalid email"));

	}
	@Override
	public User findByRoleIgnoreCase(String role) {
		return userRepository.findByRoleIgnoreCase(role).orElseThrow(()-> new NoSuchElementException("invalid role"));
	}

	@Override
	public boolean checkUniqueUserEmail(String userEmail) {
		return userRepository.existsByUseremail(userEmail);
	}

	@Override
	public User findByUserEmailAndRole(String userEmail, String admin) {
		return userRepository.findByUseremailAndRole(userEmail, admin).orElseThrow(()-> new NoSuchElementException("no admin found"));
	}

	@Override
	public User findByToken(String token) {
	return 	userRepository.findByToken(token).orElseThrow(()-> new NoSuchElementException("Wrong token, please enter valid token"));
	}


}
