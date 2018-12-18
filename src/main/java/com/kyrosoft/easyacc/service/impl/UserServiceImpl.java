package com.kyrosoft.easyacc.service.impl;

import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyrosoft.easyacc.model.User;
import com.kyrosoft.easyacc.model.criteria.UserCriteriaSpec;
import com.kyrosoft.easyacc.repository.UserRepository;
import com.kyrosoft.easyacc.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserCriteriaSpec> 
	implements UserService {
	
	private BCryptPasswordEncoder encoder;
	
	public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	protected void setValueFromValueObject(User entity, User entityVal) throws Exception {
		// TODO User Service: setValueFromValueObject
		
	}

	@Override
	protected Specifications<User> setSearchCriteria(UserCriteriaSpec criteria) {
		// TODO User Service: setSearchCriteria
		return null;
	}
	
	@Override
	public User create(User user) {
		
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		user = super.create(user);
		
		return user;
	}

}
