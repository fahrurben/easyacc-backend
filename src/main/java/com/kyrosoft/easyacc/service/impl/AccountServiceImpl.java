package com.kyrosoft.easyacc.service.impl;

import static com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec.haveId;
import static com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec.codeLike;
import static com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec.nameLike;
import static com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec.typeEqual;
import static com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec.isActiveEqual;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.AccountGroup;
import com.kyrosoft.easyacc.model.BalanceMove;
import com.kyrosoft.easyacc.model.criteria.AccountCriteriaSpec;
import com.kyrosoft.easyacc.repository.AccountGroupRepository;
import com.kyrosoft.easyacc.repository.AccountRepository;
import com.kyrosoft.easyacc.repository.BalanceMoveRepository;
import com.kyrosoft.easyacc.service.AccountService;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, AccountCriteriaSpec> 
	implements AccountService {

	private AccountGroupRepository accountGroupRepository;
	
	private BalanceMoveRepository balanceMoveRepository;
	
	public AccountServiceImpl(
			AccountRepository repository, 
			AccountGroupRepository accountGroupRepository,
			BalanceMoveRepository balanceMoveRepository) {
		this.repository = repository;
		this.accountGroupRepository = accountGroupRepository;
		this.balanceMoveRepository = balanceMoveRepository;
	}
	
	@Override
	protected void setValueFromValueObject(Account entity, Account entityVal) 
			throws Exception {
		
		entity.setName(entityVal.getName());
		entity.setNormalBalance(entityVal.getNormalBalance());
		entity.setType(entityVal.getType());
		entity.setCode(entityVal.getCode());
		
		Optional<AccountGroup> group = accountGroupRepository.findById(entityVal.getAccountGroup().getId());
		if (!group.isPresent()) {
			throw new EntityNotFoundException("Account group is not exist");
		}
		
		entity.setAccountGroup(group.get());
		entity.setIsActive(entityVal.getIsActive());
	}

	@Override
	protected Specifications<Account> setSearchCriteria(AccountCriteriaSpec criteria) {
		Specifications<Account> specs = where(haveId());
		
		if (criteria.getName() != null) {
			specs.and(nameLike(criteria.getName()));
		}
		
		if (criteria.getCode() != null) {
			specs.and(codeLike(criteria.getCode()));
		}
		
		if (criteria.getType() != null) {
			specs.and(typeEqual(criteria.getType()));
		}
		
		if (criteria.getIsActive() != null) {
			specs.and(isActiveEqual(criteria.getIsActive()));
		}
		
		return specs;
	}

	@Override
	public List<Account> getAccountsBalance() {
		
		List<Account> accounts = new ArrayList<>();
		Iterable<Account> temp = repository.findAll();
		temp.iterator().forEachRemaining(acc -> accounts.add(acc));
		
		accounts.forEach(acc -> {
			Double bal = 0.0;
			BalanceMove balMove = balanceMoveRepository.findFirstByAccountOrderByIdDesc(acc);
			bal = balMove != null ? balMove.getBalance() : bal;
			acc.setBalance(bal);
		});
		
		return accounts;
	}

}
