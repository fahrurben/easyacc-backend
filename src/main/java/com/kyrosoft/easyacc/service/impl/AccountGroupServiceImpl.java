package com.kyrosoft.easyacc.service.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.kyrosoft.easyacc.model.AccountGroup;
import com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec;
import com.kyrosoft.easyacc.repository.AccountGroupRepository;
import com.kyrosoft.easyacc.service.AccountGroupService;

import static com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec.haveId;
import static com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec.nameLike;
import static com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec.codeLike;
import static com.kyrosoft.easyacc.model.criteria.AccountGroupCriteriaSpec.isActiveEqual;

@Service
public class AccountGroupServiceImpl extends BaseServiceImpl<AccountGroup, AccountGroupCriteriaSpec> 
	implements AccountGroupService {

	public AccountGroupServiceImpl(AccountGroupRepository repository) {
		this.repository = repository;
	}
	
	@Override
	protected void setValueFromValueObject(AccountGroup entity, AccountGroup entityVal) 
		throws Exception {
		
		entity.setCode(entityVal.getCode());
		entity.setName(entityVal.getName());
		entity.setDesc(entityVal.getDesc());
		entity.setIsActive(entityVal.getIsActive());
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Specifications<AccountGroup> setSearchCriteria(AccountGroupCriteriaSpec criteria) {
		
		Specifications<AccountGroup> specs = where(haveId());
		
		if (criteria.getName() != null) {
			specs = specs.and(nameLike(criteria.getName()));
		}
		
		if (criteria.getCode() != null) {
			specs = specs.and(codeLike(criteria.getCode()));
		}
		
		if (criteria.getIsActive() != null) {
			specs = specs.and(isActiveEqual(criteria.getIsActive()));
		}
		
		return specs;
	}

}
