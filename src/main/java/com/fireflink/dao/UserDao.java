package com.fireflink.dao;

import java.util.Optional;

import com.fireflink.model.User;

public interface UserDao {

	User addUser(User user);


	User findByUseremail(String email);


	User findByRoleIgnoreCase(String role);

	boolean checkUniqueUserEmail(String userEmail);

	User findByUserEmailAndRole(String userEmail, String admin);

    User findByToken(String token);
}
