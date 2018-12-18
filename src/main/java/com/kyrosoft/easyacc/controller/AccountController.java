package com.kyrosoft.easyacc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec;
import com.kyrosoft.easyacc.service.AccountService;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

	private AccountService service;
	
	public AccountController(AccountService accountService) {
		this.service = accountService;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public Account create(@RequestBody Account account) {
		
		return service.create(account);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Account get(@PathVariable Long id) throws EntityNotFoundException {
		
		return service.get(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Account update(@PathVariable Long id, @RequestBody Account account) 
			throws Exception {
		
		return service.update(id, account);
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public List<Account> find(
			HttpServletResponse response,
			@RequestBody  AccountCriteriaSpec searchCriteria,
			@RequestParam String sortBy,
			@RequestParam String sortOrder,
			@RequestParam Integer page, 
			@RequestParam Integer size
			) {
		
		Sort sort = new Sort(sortOrder.equals("Asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		Pageable pageable = new PageRequest(page - 1, size, sort);
		
		Page<Account> pageCategories = service.find(searchCriteria, pageable);
		
		response.addHeader("total", Long.toString(pageCategories.getTotalElements()));
		response.addHeader("total-page", Integer.toString(pageCategories.getTotalPages()));
		return pageCategories.getContent();
	}
	
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public List<Account> accountsWithBalance() {
		return service.getAccountsBalance();
	}
}
