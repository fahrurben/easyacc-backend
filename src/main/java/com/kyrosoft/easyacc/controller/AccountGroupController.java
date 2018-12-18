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
import com.kyrosoft.easyacc.model.AccountGroup;
import com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec;
import com.kyrosoft.easyacc.service.AccountGroupService;

@RestController
@RequestMapping("api/accountgroups")
public class AccountGroupController {

	private AccountGroupService service;
	
	public AccountGroupController(AccountGroupService service) {
		this.service = service;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public AccountGroup create(@RequestBody AccountGroup accGroup) {
		
		return service.create(accGroup);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AccountGroup get(@PathVariable Long id) throws EntityNotFoundException {
		return service.get(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public AccountGroup update(@PathVariable Long id, @RequestBody AccountGroup accGroup) 
			throws Exception {
		
		return service.update(id, accGroup);
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public List<AccountGroup> find(
			HttpServletResponse response,
			@RequestBody AccountGroupCriteriaSpec searchCriteria,
			@RequestParam String sortBy,
			@RequestParam String sortOrder,
			@RequestParam Integer page, 
			@RequestParam Integer size
			) {
		
		Sort sort = new Sort(sortOrder.equals("Asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		Pageable pageable = new PageRequest(page - 1, size, sort);
		
		Page<AccountGroup> pageCategories = service.find(searchCriteria, pageable);
		
		response.addHeader("total", Long.toString(pageCategories.getTotalElements()));
		response.addHeader("total-page", Integer.toString(pageCategories.getTotalPages()));
		return pageCategories.getContent();
	}
}
