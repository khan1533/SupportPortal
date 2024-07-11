package com.fireflink.service.serviceimpl;

import com.fireflink.dao.UserDao;
import com.fireflink.dto.UserRequestDto;
import com.fireflink.enums.Access;
import com.fireflink.mailservice.SendEmail;
import com.fireflink.model.User;
import com.fireflink.repository.UserRepository;
import com.fireflink.service.UserService;
import com.fireflink.utility.ResponseStructure;
import com.fireflink.utility.ResponseStructureAPI;
import com.fireflink.utility.SimpleResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	private final UserRepository repository;
	private final ModelMapper modelMapper;
	private final SendEmail sendEmail;
    private final SimpleResponse simpleResponse;
	private final MongoTemplate mongoTemplate;



	public User findByRole(String role){
		return	userDao.findByRoleIgnoreCase(role);
    }

	@Override
	public User findByUseremail(String email) {
		return userDao.findByUseremail(email);
	}

	@Override
	public User findByRoleIgnoreCase(String admin) {
		return userDao.findByRoleIgnoreCase(admin);
	}

	@Override
	public String checkUniqueUserEmail(String userEmail) {
		if (userDao.checkUniqueUserEmail(userEmail)) {
			return "Duplicate";
		} else
			return "Success";
	}

	@Override
	public ResponseEntity<SimpleResponse> addUser(UserRequestDto userRequestDto, String userEmail) {
		if(userRequestDto.getRole().equalsIgnoreCase("admin")){
			if(userDao.findByRoleIgnoreCase("admin")!=null){
				throw new NoSuchElementException("admin Already Exists");
			} else
			return 	 createUser(userRequestDto);

		} else {
			User user= userDao.findByUserEmailAndRole(userEmail,"admin");
			return createUser(userRequestDto);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure> setPassword(String token, String password) {
	User user=	userDao.findByToken(token);
		Query query=new Query(Criteria.where("token").is(token));
		Update  update=new Update().unset("token");
		mongoTemplate.updateFirst(query, update, User.class);
	user.setPassword(password);
	user.setStatus("active");
	if(user.getRole().equalsIgnoreCase("admin")){
		user.createBaseEntity(user.getUsername(), user.getUseremail());
		user.setAccess(Access.FULL_ACCESS.toString());
	}
	else{
		Query query1=new Query(Criteria.where("role").regex("admin", "i"));
		User admin=mongoTemplate.findOne(query1, User.class);
		user.createBaseEntity(admin.getUsername(), admin.getUseremail());
		user.setAccess(Access.VIEW_ACCESS.toString());

	}

	user= userDao.addUser(user);
	return ResponseStructureAPI.createResponse(user);
	}

	private ResponseEntity<SimpleResponse> createUser(UserRequestDto userRequestDto) {
		
		User user=modelMapper.map(userRequestDto, User.class);
	String	token=UUID.randomUUID().toString();
	String[] recievers={user.getUseremail()};

    try{
	sendEmail.createMail(recievers," Set password"+"this is Ur toke "+token, "please varify0"  );
    } catch (MessagingException e) {

        e.printStackTrace();
    }

    user.setStatus("Activation pending");
    user.setToken(token);
    userDao.addUser(user);
    simpleResponse.setMessage("varification token sent to your email");
    simpleResponse.setStatus(HttpStatus.OK.value());
    return  ResponseEntity.ok(simpleResponse);
	}


}
