package com.fireflink.repository;

import java.util.Optional;

import com.fireflink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Integer>{

	Optional<User> findByUsername(String name);

	Optional<User> findByUseremail(String email);

	Optional<User> findByRoleIgnoreCase(String role);

	boolean existsByUseremail(String userEmail);

	Optional<User> findByUseremailAndRole(String userEmail, String admin);

	Optional<User> findByToken(String token);
}
