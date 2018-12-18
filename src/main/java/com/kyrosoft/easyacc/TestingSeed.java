package com.kyrosoft.easyacc;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.AccountGroup;
import com.kyrosoft.easyacc.model.AccountType;
import com.kyrosoft.easyacc.model.NormalBalance;
import com.kyrosoft.easyacc.model.User;
import com.kyrosoft.easyacc.service.AccountGroupService;
import com.kyrosoft.easyacc.service.AccountService;
import com.kyrosoft.easyacc.service.UserService;

@Component
public class TestingSeed {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountGroupService accountGroupService;
	
	@Autowired
	private AccountService accountService;
	
	@EventListener
    @Transactional
    public void seed(ContextRefreshedEvent event) {
		
		User user = new User();
		user.setEmail("test@test.com");
		user.setFirstName("test");
		user.setLastName("test");
		user.setLastName("test");
		user.setPassword("test");
		user.setIsActive(true);
		
		userService.create(user);
		
		AccountGroup accGroup = new AccountGroup();
		accGroup.setCode("1");
		accGroup.setName("test");
		accGroup.setDesc("test");
		accGroup.setIsActive(true);
		
		accountGroupService.create(accGroup);
		
		Account acc = new Account();
		acc.setCode("101");
		acc.setAccountGroup(accGroup);
		acc.setName("test");
		acc.setNormalBalance(NormalBalance.CREDIT);
		acc.setType(AccountType.ASSETS);
		acc.setIsActive(true);
		
		accountService.create(acc);
	}
	
	
}
